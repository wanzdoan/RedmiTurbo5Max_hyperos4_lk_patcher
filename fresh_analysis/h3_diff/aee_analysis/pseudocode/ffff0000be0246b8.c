
void FUN_ffff0000be0246b8(code *param_1)

{
  char cVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 *puVar4;
  ulong uVar5;
  char *pcVar6;
  
  cVar1 = FUN_ffff0000be065018();
  if (cVar1 != '\x06') {
    uVar3 = FUN_ffff0000be03572c();
    iVar2 = FUN_ffff0000be060cb8(uVar3,s_rst_from__lk_ffff0000be0a7dba);
    if (iVar2 != 0) {
      return;
    }
  }
  puVar4 = (undefined8 *)FUN_ffff0000be01cb9c(s_BL33_reserved_ffff0000be0c1a76,0);
  if (puVar4 == (undefined8 *)0x0) {
    pcVar6 = s_reserved_memory_BL33_reserved_do_ffff0000be0a7dc7;
  }
  else {
    FUN_ffff0000be05fd98(s_bl33_addr__llx_ffff0000be0c1a84,*puVar4);
    uVar5 = (*param_1)(*puVar4,0xc00000);
    pcVar6 = s_AEE_EXPDB_for_bl33_memdump_done_ffff0000be0bbdce;
    if ((uVar5 & 1) == 0) {
      pcVar6 = s_AEE_EXPDB_for_bl33_memdump_fails_ffff0000be0a7df5;
    }
  }
  FUN_ffff0000be05fd98(pcVar6);
  return;
}


