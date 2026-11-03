
/* RmcCallControlCommonRequestHandler::~RmcCallControlCommonRequestHandler() */

void __thiscall
RmcCallControlCommonRequestHandler::~RmcCallControlCommonRequestHandler
          (RmcCallControlCommonRequestHandler *this)

{
  long *plVar1;
  
  plVar1 = *(long **)(this + 0x22a0);
  *(undefined **)this = PTR_vtable_005d4430 + 0x10;
  if (plVar1 != (long *)0x0) {
    (**(code **)(*plVar1 + 8))(plVar1);
  }
  RmcCallControlBaseHandler::~RmcCallControlBaseHandler((RmcCallControlBaseHandler *)this);
  return;
}


