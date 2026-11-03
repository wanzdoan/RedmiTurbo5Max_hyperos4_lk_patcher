// ffff000090781d64  size=208  FUN_ffff000090781d64

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090781d64(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  undefined8 local_30;
  undefined4 local_28;
  undefined4 uStack_24;
  
  iVar1 = FUN_ffff00009076da24(param_1,s__chosen_ffff0000907ea70b);
  if (iVar1 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s_get__chosen_offset_failed__d__ffff0000907db82b,iVar1);
  }
  local_30 = _DAT_ffff000090887224;
  local_28 = _DAT_ffff00009088722c;
  uStack_24 = _DAT_ffff000090887230;
  iVar2 = FUN_ffff00009076e6d4(param_1,iVar1,s_ram_console_ffff0000907f7283,&local_30,0x10);
  if (iVar2 == 0) {
    local_30 = *(undefined8 *)(_DAT_ffff000090887110 + 0xc);
    iVar1 = FUN_ffff00009076e6d4(param_1,iVar1,s_log_store_ffff0000907d5a9f,&local_30,8);
    if (iVar1 == 0) {
      return;
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s_log_store_dts_write_failed_ffff0000907eb848);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0(s_dts_write_failed_ffff0000907eb837);
}


