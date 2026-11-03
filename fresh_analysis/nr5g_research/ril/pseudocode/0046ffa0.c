
/* RmcNetworkHandler::isNrSupported() */

void __thiscall RmcNetworkHandler::isNrSupported(RmcNetworkHandler *this)

{
  long lVar1;
  undefined *puVar2;
  int iVar3;
  char local_38 [5];
  char acStack_33 [3];
  char acStack_30 [8];
  long local_28;
  
  puVar2 = PTR_nr_support_005d3e70;
  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  iVar3 = *(int *)PTR_nr_support_005d3e70;
  local_38 = (char  [5])s_5G_SUPPORTED_0021e236._0_5_;
  acStack_33 = (char  [3])s_5G_SUPPORTED_0021e236._5_3_;
  acStack_30._0_5_ = s_5G_SUPPORTED_0021e236._8_5_;
  if (iVar3 < 0) {
    iVar3 = RfxBaseHandler::getFeatureVersion((RfxBaseHandler *)this,local_38,0);
    *(int *)puVar2 = iVar3;
  }
  if (*(long *)(lVar1 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(iVar3 == 1);
}


