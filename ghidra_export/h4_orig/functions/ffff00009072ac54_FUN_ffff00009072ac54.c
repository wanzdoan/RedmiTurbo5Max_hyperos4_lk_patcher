// ffff00009072ac54  size=176  FUN_ffff00009072ac54

void FUN_ffff00009072ac54(code *param_1)

{
  char cVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 *puVar4;
  ulong uVar5;
  char *pcVar6;
  
  cVar1 = FUN_ffff0000907833f8();
  if (cVar1 != '\a') {
    uVar3 = FUN_ffff000090746094();
    iVar2 = FUN_ffff00009077f0ac(uVar3,s_rst_from__BL2EXT_ffff0000907d42fd);
    if (iVar2 != 0) {
      return;
    }
  }
  puVar4 = (undefined8 *)FUN_ffff00009071a348(s_system_bl2_ext_ffff0000907eabb4,0);
  if (puVar4 == (undefined8 *)0x0) {
    pcVar6 = s_Reserved_memory_bl2_ext_reserved_ffff0000907d29db;
  }
  else {
    FUN_ffff00009077e11c(s_bl2_ext_addr__llx_ffff0000907d2a0c,*puVar4);
    uVar5 = (*param_1)(*puVar4,0x400000);
    pcVar6 = s_AEE_EXPDB_for_bl2_ext_memdump_do_ffff0000907fd131;
    if ((uVar5 & 1) == 0) {
      pcVar6 = s_AEE_EXPDB_for_bl2_ext_memdump_fa_ffff0000907fb5db;
    }
  }
  FUN_ffff00009077e11c(pcVar6);
  return;
}


