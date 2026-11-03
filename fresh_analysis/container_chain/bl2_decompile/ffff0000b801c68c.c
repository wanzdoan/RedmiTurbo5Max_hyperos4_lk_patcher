
void FUN_ffff0000b801c68c(code *param_1)

{
  char cVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 *puVar4;
  ulong uVar5;
  char *pcVar6;
  
  cVar1 = FUN_ffff0000b8050128();
  if (cVar1 != '\x06') {
    uVar3 = FUN_ffff0000b802c148();
    iVar2 = FUN_ffff0000b804d248(uVar3,s_rst_from__lk_ffff0000b8088db8);
    if (iVar2 != 0) {
      return;
    }
  }
  puVar4 = (undefined8 *)FUN_ffff0000b800db20(s_BL33_reserved_ffff0000b809d396,0);
  if (puVar4 == (undefined8 *)0x0) {
    pcVar6 = s_reserved_memory_BL33_reserved_do_ffff0000b808898c;
  }
  else {
    FUN_ffff0000b804c33c(s_bl33_addr__llx_ffff0000b809d667,*puVar4);
    uVar5 = (*param_1)(*puVar4,0xc00000);
    pcVar6 = s_AEE_EXPDB_for_bl33_memdump_done_ffff0000b8098bc1;
    if ((uVar5 & 1) == 0) {
      pcVar6 = s_AEE_EXPDB_for_bl33_memdump_fails_ffff0000b8088dc5;
    }
  }
  FUN_ffff0000b804c33c(pcVar6);
  return;
}


