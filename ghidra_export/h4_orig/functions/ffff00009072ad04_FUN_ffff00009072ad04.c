// ffff00009072ad04  size=176  FUN_ffff00009072ad04

void FUN_ffff00009072ad04(code *param_1)

{
  char cVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 *puVar4;
  ulong uVar5;
  char *pcVar6;
  
  cVar1 = FUN_ffff0000907833f8();
  if (cVar1 != '\x06') {
    uVar3 = FUN_ffff000090746094();
    iVar2 = FUN_ffff00009077f0ac(uVar3,s_rst_from__lk_ffff0000907d5e58);
    if (iVar2 != 0) {
      return;
    }
  }
  puVar4 = (undefined8 *)FUN_ffff00009071a348(s_BL33_reserved_ffff0000907fb21c,0);
  if (puVar4 == (undefined8 *)0x0) {
    pcVar6 = s_reserved_memory_BL33_reserved_do_ffff0000907d5e65;
  }
  else {
    FUN_ffff00009077e11c(s_bl33_addr__llx_ffff0000907fb600,*puVar4);
    uVar5 = (*param_1)(*puVar4,0xc00000);
    pcVar6 = s_AEE_EXPDB_for_bl33_memdump_done_ffff0000907f298d;
    if ((uVar5 & 1) == 0) {
      pcVar6 = s_AEE_EXPDB_for_bl33_memdump_fails_ffff0000907d5e93;
    }
  }
  FUN_ffff00009077e11c(pcVar6);
  return;
}


