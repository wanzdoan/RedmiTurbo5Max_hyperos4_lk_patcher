
/* RmcCapabilitySwitchRequestHandler::queryNrType() */

void RmcCapabilitySwitchRequestHandler::queryNrType(void)

{
  long lVar1;
  int iVar2;
  undefined8 local_30;
  long local_28;
  
  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  iVar2 = RatConfig_isNrSupported();
  android::String8::format("%d",&local_30,(ulong)(iVar2 != 0));
  rfx_property_set("vendor.ril.nr.type",local_30);
  android::String8::~String8((String8 *)&local_30);
  if (*(long *)(lVar1 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


