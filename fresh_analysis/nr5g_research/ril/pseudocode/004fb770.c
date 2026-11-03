
/* virtual thunk to RmcDcCommonReqHandler::~RmcDcCommonReqHandler() */

void __thiscall RmcDcCommonReqHandler::~RmcDcCommonReqHandler(RmcDcCommonReqHandler *this)

{
  long lVar1;
  
  lVar1 = *(long *)(*(long *)this + -0x18);
  android::RefBase::~RefBase((RefBase *)(this + lVar1 + 0x40));
  operator_delete(this + lVar1);
  return;
}


