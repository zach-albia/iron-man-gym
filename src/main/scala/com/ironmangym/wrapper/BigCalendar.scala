package com.ironmangym.wrapper

import japgolly.scalajs.react._
import japgolly.scalajs.react.raw.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

object BigCalendar {

  @JSImport("react-big-calendar", JSImport.Namespace)
  @js.native
  private object RawComponent extends js.Object

  @JSImport("moment", JSImport.Namespace)
  @js.native
  private object moment extends js.Object

  @js.native
  trait Event extends DateRange {
    var title: String = js.native
    var allDay: js.UndefOr[Boolean] = js.native
    var resource: js.UndefOr[js.Any] = js.native
  }

  @js.native
  trait DateRange extends js.Object {
    var start: js.Date = js.native
    var end: js.Date = js.native
  }

  @js.native
  trait Bounds extends js.Object {
    var x: Int = js.native
    var y: Int = js.native
    var top: Int = js.native
    var right: Int = js.native
    var left: Int = js.native
    var bottom: Int = js.native
  }

  @js.native
  trait Box extends js.Object {
    var clientX: Int = js.native
    var clientY: Int = js.native
    var x: Int = js.native
    var y: Int = js.native
  }

  @js.native
  trait SlotInfo extends DateRange {
    var slots: js.Array[js.Date] = js.native
    var action: String = js.native
    var bounds: Bounds = js.native
    var box: Box = js.native
  }

  @js.native
  trait Views extends js.Object {
    var month: Boolean | ReactElement = js.native
    var week: Boolean | ReactElement = js.native
    var day: Boolean | ReactElement = js.native
    var agenda: Boolean | ReactElement = js.native
  }

  @js.native
  trait PopupOffset extends js.Object {
    var x: Int = js.native
    var y: Int = js.native
  }

  @js.native
  trait EventNodeStyle extends js.Object {
    var className: js.UndefOr[String] = js.native
    var style: js.UndefOr[js.Any] = js.native
  }

  val ignoreEvents = "ignoreEvents"

  def event(
      title:    String,
      start:    js.Date,
      end:      js.Date,
      allDay:   js.UndefOr[Boolean] = js.undefined,
      resource: js.UndefOr[js.Any]  = js.undefined
  ): Event = {
    val e = js.Dynamic.literal(
      title = title,
      start = start,
      end   = end
    )
    allDay.foreach(e.updateDynamic("allDay")(_))
    resource.foreach(e.updateDynamic("resource")(_))
    e.asInstanceOf[Event]
  }

  @js.native
  trait Props extends js.Object {
    var elementProps: js.UndefOr[js.Object] = js.native
    var date: js.UndefOr[js.Date] = js.native
    var view: js.UndefOr[String] = js.native
    var defaultView: js.UndefOr[String] = js.native
    var events: js.Array[Event] = js.native
    var titleAccessor: js.UndefOr[js.Function1[Event, String]] = js.native
    var tooltipAccessor: js.UndefOr[js.Function1[Event, String]] = js.native
    var allDayAccessor: js.UndefOr[js.Function1[Event, String]] = js.native
    var startAccessor: js.UndefOr[js.Function1[Event, String]] = js.native
    var endAccessor: js.UndefOr[js.Function1[Event, String]] = js.native
    var resourceAccessor: js.UndefOr[js.Function1[Event, String]] = js.native
    var resources: js.UndefOr[js.Any] = js.native
    var resourceIdAccessor: js.UndefOr[js.Function1[Event, String]] = js.native
    var resourceTitleAccessor: js.UndefOr[js.Function1[Event, String]] = js.native
    var getNow: js.UndefOr[js.Function0[js.Date]] = js.native
    var onNavigate: js.UndefOr[js.Function1[js.Date, Unit]] = js.native
    var onView: js.UndefOr[js.Function1[String, Unit]] = js.native
    var onDrillDown: js.UndefOr[js.Function0[Unit]] = js.native
    var onRangeChange: js.UndefOr[js.Function1[js.Array[js.Date] | DateRange, Unit]] = js.native
    var onSelectSlot: js.UndefOr[js.Function1[SlotInfo, js.Any]] = js.native
    var onSelectEvent: js.UndefOr[js.Function2[Event, ReactEvent, js.Any]] = js.native
    var onDoubleClickEvent: js.UndefOr[js.Function2[Event, ReactMouseEvent, Unit]] = js.native
    var onSelecting: js.UndefOr[js.Function1[DateRange, Boolean]] = js.native
    var selected: js.UndefOr[Event] = js.native
    var views: js.UndefOr[js.Array[String] | Views] = js.native
    var drilldownView: js.UndefOr[String] = js.native
    var getDrilldownView: js.UndefOr[js.Function3[js.Date, String, js.Array[String], String | Null]] = js.native
    var length: js.UndefOr[Int] = js.native
    var toolbar: js.UndefOr[Boolean] = js.native
    var popup: js.UndefOr[Boolean] = js.native
    var popupOffset: js.UndefOr[Int | PopupOffset] = js.native
    var selectable: js.UndefOr[Boolean | ignoreEvents.type] = js.native
    var resizable: js.UndefOr[Boolean] = js.native
    var longPressThreshold: js.UndefOr[Int] = js.native
    var step: js.UndefOr[Int] = js.native
    var timeslots: js.UndefOr[Int] = js.native
    var rtl: js.UndefOr[Boolean] = js.native
    var eventPropGetter: js.UndefOr[js.Function4[Event, js.Date, js.Date, Boolean, EventNodeStyle]] = js.native
    var slotPropGetter: js.UndefOr[js.Function1[js.Date, EventNodeStyle]] = js.native
    var dayPropGetter: js.UndefOr[js.Function1[js.Date, EventNodeStyle]] = js.native
    var showMultiDayTimes: js.UndefOr[Boolean] = js.native
    var min: js.UndefOr[js.Date] = js.native
    var max: js.UndefOr[js.Date] = js.native
    var scrollToTime: js.UndefOr[js.Date] = js.native
    var culture: js.UndefOr[String] = js.native
    var formats: js.UndefOr[js.Object] = js.native
    var components: js.UndefOr[js.Object] = js.native
    var messages: js.UndefOr[js.Object] = js.native
  }

  private def props(
      elementProps:          js.UndefOr[js.Object],
      date:                  js.UndefOr[js.Date],
      view:                  js.UndefOr[String],
      defaultView:           js.UndefOr[String],
      events:                js.Array[Event],
      titleAccessor:         js.UndefOr[js.Function1[Event, String]],
      tooltipAccessor:       js.UndefOr[js.Function1[Event, String]],
      allDayAccessor:        js.UndefOr[js.Function1[Event, String]],
      startAccessor:         js.UndefOr[js.Function1[Event, String]],
      endAccessor:           js.UndefOr[js.Function1[Event, String]],
      resourceAccessor:      js.UndefOr[js.Function1[Event, String]],
      resources:             js.UndefOr[js.Any],
      resourceIdAccessor:    js.UndefOr[js.Function1[Event, String]],
      resourceTitleAccessor: js.UndefOr[js.Function1[Event, String]],
      getNow:                js.UndefOr[js.Function0[js.Date]],
      onNavigate:            js.UndefOr[js.Function1[js.Date, Unit]],
      onView:                js.UndefOr[js.Function1[String, Unit]],
      onDrillDown:           js.UndefOr[js.Function0[Unit]],
      onRangeChange:         js.UndefOr[js.Function1[js.Array[js.Date] | DateRange, Unit]],
      onSelectSlot:          js.UndefOr[js.Function1[SlotInfo, js.Any]],
      onSelectEvent:         js.UndefOr[js.Function2[Event, ReactEvent, js.Any]],
      onDoubleClickEvent:    js.UndefOr[js.Function2[Event, ReactMouseEvent, Unit]],
      onSelecting:           js.UndefOr[js.Function1[DateRange, Boolean]],
      selected:              js.UndefOr[Event],
      views:                 js.UndefOr[js.Array[String] | Views],
      drilldownView:         js.UndefOr[String],
      getDrilldownView:      js.UndefOr[js.Function3[js.Date, String, js.Array[String], String | Null]],
      length:                js.UndefOr[Int],
      toolbar:               js.UndefOr[Boolean],
      popup:                 js.UndefOr[Boolean],
      popupOffset:           js.UndefOr[Int | PopupOffset],
      selectable:            js.UndefOr[Boolean | ignoreEvents.type],
      resizable:             js.UndefOr[Boolean],
      longPressThreshold:    js.UndefOr[Int],
      step:                  js.UndefOr[Int],
      timeslots:             js.UndefOr[Int],
      rtl:                   js.UndefOr[Boolean],
      eventPropGetter:       js.UndefOr[js.Function4[Event, js.Date, js.Date, Boolean, EventNodeStyle]],
      slotPropGetter:        js.UndefOr[js.Function1[js.Date, EventNodeStyle]],
      dayPropGetter:         js.UndefOr[js.Function1[js.Date, EventNodeStyle]],
      showMultiDayTimes:     js.UndefOr[Boolean],
      min:                   js.UndefOr[js.Date],
      max:                   js.UndefOr[js.Date],
      scrollToTime:          js.UndefOr[js.Date],
      culture:               js.UndefOr[String],
      formats:               js.UndefOr[js.Object],
      components:            js.UndefOr[js.Object],
      messages:              js.UndefOr[js.Object]
  ): Props = {
    val p = js.Dynamic.literal(events = events)
    elementProps.foreach(p.updateDynamic("elementProps")(_))
    date.foreach(p.updateDynamic("date")(_))
    view.foreach(p.updateDynamic("view")(_))
    defaultView.foreach(p.updateDynamic("defaultView")(_))
    titleAccessor.foreach(p.updateDynamic("titleAccessor")(_))
    tooltipAccessor.foreach(p.updateDynamic("tooltipAccessor")(_))
    allDayAccessor.foreach(p.updateDynamic("allDayAccessor")(_))
    startAccessor.foreach(p.updateDynamic("startAccessor")(_))
    endAccessor.foreach(p.updateDynamic("endAccessor")(_))
    resourceAccessor.foreach(p.updateDynamic("resourceAccessor")(_))
    resources.foreach(p.updateDynamic("resources")(_))
    resourceIdAccessor.foreach(p.updateDynamic("resourceIdAccessor")(_))
    resourceTitleAccessor.foreach(p.updateDynamic("resourceTitleAccessor")(_))
    getNow.foreach(p.updateDynamic("getNow")(_))
    onNavigate.foreach(p.updateDynamic("onNavigate")(_))
    onView.foreach(p.updateDynamic("onView")(_))
    onDrillDown.foreach(p.updateDynamic("onDrillDown")(_))
    onRangeChange.foreach(p.updateDynamic("onRangeChange")(_))
    onSelectSlot.foreach(p.updateDynamic("onSelectSlot")(_))
    onSelectEvent.foreach(p.updateDynamic("onSelectEvent")(_))
    onDoubleClickEvent.foreach(p.updateDynamic("onDoubleClickEvent")(_))
    onSelecting.foreach(p.updateDynamic("onSelecting")(_))
    selected.foreach(p.updateDynamic("selected")(_))
    views.foreach(v => p.updateDynamic("views")(v.asInstanceOf[js.Any]))
    drilldownView.foreach(p.updateDynamic("drilldownView")(_))
    getDrilldownView.foreach(p.updateDynamic("getDrilldownView")(_))
    length.foreach(p.updateDynamic("length")(_))
    toolbar.foreach(p.updateDynamic("toolbar")(_))
    popup.foreach(p.updateDynamic("popup")(_))
    popupOffset.foreach(v => p.updateDynamic("popupOffset")(v.asInstanceOf[js.Any]))
    selectable.foreach(v => p.updateDynamic("selectable")(v.asInstanceOf[js.Any]))
    resizable.foreach(p.updateDynamic("resizable")(_))
    longPressThreshold.foreach(p.updateDynamic("longPressThreshold")(_))
    step.foreach(p.updateDynamic("step")(_))
    timeslots.foreach(p.updateDynamic("timeslots")(_))
    rtl.foreach(p.updateDynamic("rtl")(_))
    eventPropGetter.foreach(p.updateDynamic("eventPropGetter")(_))
    slotPropGetter.foreach(p.updateDynamic("slotPropGetter")(_))
    dayPropGetter.foreach(p.updateDynamic("dayPropGetter")(_))
    showMultiDayTimes.foreach(p.updateDynamic("showMultiDayTimes")(_))
    min.foreach(p.updateDynamic("min")(_))
    max.foreach(p.updateDynamic("max")(_))
    scrollToTime.foreach(p.updateDynamic("scrollToTime")(_))
    culture.foreach(p.updateDynamic("culture")(_))
    formats.foreach(p.updateDynamic("formats")(_))
    components.foreach(p.updateDynamic("components")(_))
    messages.foreach(p.updateDynamic("messages")(_))
    p.asInstanceOf[Props]
  }

  private val component = JsComponent[Props, Children.None, Null](RawComponent)

  def apply(
      elementProps:          js.UndefOr[js.Object]                                                      = js.undefined,
      date:                  js.UndefOr[js.Date]                                                        = js.undefined,
      view:                  js.UndefOr[String]                                                         = js.undefined,
      defaultView:           js.UndefOr[String]                                                         = js.undefined,
      events:                js.Array[Event],
      titleAccessor:         js.UndefOr[js.Function1[Event, String]]                                    = js.undefined,
      tooltipAccessor:       js.UndefOr[js.Function1[Event, String]]                                    = js.undefined,
      allDayAccessor:        js.UndefOr[js.Function1[Event, String]]                                    = js.undefined,
      startAccessor:         js.UndefOr[js.Function1[Event, String]]                                    = js.undefined,
      endAccessor:           js.UndefOr[js.Function1[Event, String]]                                    = js.undefined,
      resourceAccessor:      js.UndefOr[js.Function1[Event, String]]                                    = js.undefined,
      resources:             js.UndefOr[js.Any]                                                         = js.undefined,
      resourceIdAccessor:    js.UndefOr[js.Function1[Event, String]]                                    = js.undefined,
      resourceTitleAccessor: js.UndefOr[js.Function1[Event, String]]                                    = js.undefined,
      getNow:                js.UndefOr[js.Function0[js.Date]]                                          = js.undefined,
      onNavigate:            js.UndefOr[js.Function1[js.Date, Unit]]                                    = js.undefined,
      onView:                js.UndefOr[js.Function1[String, Unit]]                                     = js.undefined,
      onDrillDown:           js.UndefOr[js.Function0[Unit]]                                             = js.undefined,
      onRangeChange:         js.UndefOr[js.Function1[js.Array[js.Date] | DateRange, Unit]]              = js.undefined,
      onSelectSlot:          js.UndefOr[js.Function1[SlotInfo, js.Any]]                                 = js.undefined,
      onSelectEvent:         js.UndefOr[js.Function2[Event, ReactEvent, js.Any]]                        = js.undefined,
      onDoubleClickEvent:    js.UndefOr[js.Function2[Event, ReactMouseEvent, Unit]]                     = js.undefined,
      onSelecting:           js.UndefOr[js.Function1[DateRange, Boolean]]                               = js.undefined,
      selected:              js.UndefOr[Event]                                                          = js.undefined,
      views:                 js.UndefOr[js.Array[String] | Views]                                       = js.undefined,
      drilldownView:         js.UndefOr[String]                                                         = js.undefined,
      getDrilldownView:      js.UndefOr[js.Function3[js.Date, String, js.Array[String], String | Null]] = js.undefined,
      length:                js.UndefOr[Int]                                                            = js.undefined,
      toolbar:               js.UndefOr[Boolean]                                                        = js.undefined,
      popup:                 js.UndefOr[Boolean]                                                        = js.undefined,
      popupOffset:           js.UndefOr[Int | PopupOffset]                                              = js.undefined,
      selectable:            js.UndefOr[Boolean | ignoreEvents.type]                                    = js.undefined,
      resizable:             js.UndefOr[Boolean]                                                        = js.undefined,
      longPressThreshold:    js.UndefOr[Int]                                                            = js.undefined,
      step:                  js.UndefOr[Int]                                                            = js.undefined,
      timeslots:             js.UndefOr[Int]                                                            = js.undefined,
      rtl:                   js.UndefOr[Boolean]                                                        = js.undefined,
      eventPropGetter:       js.UndefOr[js.Function4[Event, js.Date, js.Date, Boolean, EventNodeStyle]] = js.undefined,
      slotPropGetter:        js.UndefOr[js.Function1[js.Date, EventNodeStyle]]                          = js.undefined,
      dayPropGetter:         js.UndefOr[js.Function1[js.Date, EventNodeStyle]]                          = js.undefined,
      showMultiDayTimes:     js.UndefOr[Boolean]                                                        = js.undefined,
      min:                   js.UndefOr[js.Date]                                                        = js.undefined,
      max:                   js.UndefOr[js.Date]                                                        = js.undefined,
      scrollToTime:          js.UndefOr[js.Date]                                                        = js.undefined,
      culture:               js.UndefOr[String]                                                         = js.undefined,
      formats:               js.UndefOr[js.Object]                                                      = js.undefined,
      components:            js.UndefOr[js.Object]                                                      = js.undefined,
      messages:              js.UndefOr[js.Object]                                                      = js.undefined
  ) = {
    val rc = RawComponent.asInstanceOf[js.Dynamic]
    rc.setLocalizer(rc.momentLocalizer(moment))
    component(props(
      elementProps,
      date,
      view,
      defaultView,
      events,
      titleAccessor,
      tooltipAccessor,
      allDayAccessor,
      startAccessor,
      endAccessor,
      resourceAccessor,
      resources,
      resourceIdAccessor,
      resourceTitleAccessor,
      getNow,
      onNavigate,
      onView,
      onDrillDown,
      onRangeChange,
      onSelectSlot,
      onSelectEvent,
      onDoubleClickEvent,
      onSelecting,
      selected,
      views,
      drilldownView,
      getDrilldownView,
      length,
      toolbar,
      popup,
      popupOffset,
      selectable,
      resizable,
      longPressThreshold,
      step,
      timeslots,
      rtl,
      eventPropGetter,
      slotPropGetter,
      dayPropGetter,
      showMultiDayTimes,
      min,
      max,
      scrollToTime,
      culture,
      formats,
      components,
      messages
    ))
  }
}
