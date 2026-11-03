// ffff00009072aac0  size=176  FUN_ffff00009072aac0

void FUN_ffff00009072aac0(code *param_1)

{
  char cVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 *puVar4;
  ulong uVar5;
  char *pcVar6;
  
  cVar1 = FUN_ffff000090783130();
  if (cVar1 != '\x06') {
    uVar3 = FUN_ffff000090745dfc();
    iVar2 = FUN_ffff00009077ede4(uVar3,s_rst_from__lk_ffff0000907d5dc2);
    if (iVar2 != 0) {
      return;
    }
  }
  puVar4 = (undefined8 *)FUN_ffff00009071a204(s_BL33_reserved_ffff0000907fb080,0);
  if (puVar4 == (undefined8 *)0x0) {
    pcVar6 = s_reserved_memory_BL33_reserved_do_ffff0000907d5dcf;
  }
  else {
    FUN_ffff00009077de54(s_bl33_addr__llx_ffff0000907fb464,*puVar4);
    uVar5 = (*param_1)(*puVar4,0xc00000);
    pcVar6 = s_AEE_EXPDB_for_bl33_memdump_done_ffff0000907f2801;
    if ((uVar5 & 1) == 0) {
      pcVar6 = s_AEE_EXPDB_for_bl33_memdump_fails_ffff0000907d5dfd;
    }
  }
  FUN_ffff00009077de54(pcVar6);
  return;
}


