
void FUN_ffff0000b801c5dc(code *param_1)

{
  char cVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 *puVar4;
  ulong uVar5;
  char *pcVar6;
  
  cVar1 = FUN_ffff0000b8050128();
  if (cVar1 != '\a') {
    uVar3 = FUN_ffff0000b802c148();
    iVar2 = FUN_ffff0000b804d248(uVar3,s_rst_from__BL2EXT_ffff0000b808804a);
    if (iVar2 != 0) {
      return;
    }
  }
  puVar4 = (undefined8 *)FUN_ffff0000b800db20(s_system_bl2_ext_ffff0000b809443f,0);
  if (puVar4 == (undefined8 *)0x0) {
    pcVar6 = s_Reserved_memory_bl2_ext_reserved_ffff0000b80870f8;
  }
  else {
    FUN_ffff0000b804c33c(s_bl2_ext_addr__llx_ffff0000b8087129,*puVar4);
    uVar5 = (*param_1)(*puVar4,0x400000);
    pcVar6 = s_AEE_EXPDB_for_bl2_ext_memdump_do_ffff0000b809e449;
    if ((uVar5 & 1) == 0) {
      pcVar6 = s_AEE_EXPDB_for_bl2_ext_memdump_fa_ffff0000b809d642;
    }
  }
  FUN_ffff0000b804c33c(pcVar6);
  return;
}


