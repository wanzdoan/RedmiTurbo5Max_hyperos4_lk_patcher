
/* RmcVtCommonReqHandler::RmcVtCommonReqHandler(int, int) */

void __thiscall
RmcVtCommonReqHandler::RmcVtCommonReqHandler(RmcVtCommonReqHandler *this,int param_1,int param_2)

{
  undefined *puVar1;
  long lVar2;
  ulong uVar3;
  int local_44;
  undefined8 local_40;
  long local_38;
  
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  puVar1 = PTR_vtable_005d35d8 + 0x10;
  *(int *)(this + 8) = param_1;
  *(int *)(this + 0xc) = param_2;
  *(undefined **)this = puVar1;
  uVar3 = __rfx_is_gt_mode();
  if ((uVar3 & 1) == 0) {
    mtkLogI("VT RIL CMN RMC","[RMC VT REQ HDLR] RmcVtCommonReqHandler create (slot_id = %d)",param_1
           );
  }
  else {
    local_40 = 0;
    android::String8::format("%s%s",&local_40,&DAT_001fbebf,"VT RIL CMN RMC");
    mtkLogI(local_40,"[RMC VT REQ HDLR] RmcVtCommonReqHandler create (slot_id = %d)",param_1);
    android::String8::~String8((String8 *)&local_40);
  }
  local_40 = CONCAT44(local_40._4_4_,0xe693);
  RfxBaseHandler::registerToHandleEvent((RfxBaseHandler *)this,(int *)&local_40,1);
  local_44 = 0xe692;
  RfxBaseHandler::registerToHandleRequest((RfxBaseHandler *)this,&local_44,1);
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


