
void FUN_ffff0000be024608(code *param_1)

{
  char cVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 *puVar4;
  ulong uVar5;
  char *pcVar6;
  
  cVar1 = FUN_ffff0000be065018();
  if (cVar1 != '\a') {
    uVar3 = FUN_ffff0000be03572c();
    iVar2 = FUN_ffff0000be060cb8(uVar3,s_rst_from__BL2EXT_ffff0000be0a6c91);
    if (iVar2 != 0) {
      return;
    }
  }
  puVar4 = (undefined8 *)FUN_ffff0000be01cb9c(s_system_bl2_ext_ffff0000be0b6420,0);
  if (puVar4 == (undefined8 *)0x0) {
    pcVar6 = s_Reserved_memory_bl2_ext_reserved_ffff0000be0a5984;
  }
  else {
    FUN_ffff0000be05fd98(s_bl2_ext_addr__llx_ffff0000be0a59b5,*puVar4);
    uVar5 = (*param_1)(*puVar4,0x400000);
    pcVar6 = s_AEE_EXPDB_for_bl2_ext_memdump_do_ffff0000be0c2b6d;
    if ((uVar5 & 1) == 0) {
      pcVar6 = s_AEE_EXPDB_for_bl2_ext_memdump_fa_ffff0000be0c1a51;
    }
  }
  FUN_ffff0000be05fd98(pcVar6);
  return;
}


