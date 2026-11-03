
bool FUN_ffff0000be0658b0(void)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  ulong uVar4;
  long lVar5;
  char *pcVar6;
  undefined8 uVar7;
  undefined *puVar8;
  
  lVar3 = FUN_ffff0000be01fa8c(s_aee_encrypt_ffff0000be0acf9b);
  uVar4 = FUN_ffff0000be0658a0();
  if ((uVar4 & 1) == 0) {
    puVar8 = &DAT_ffff0000be0bac01;
  }
  else {
    FUN_ffff0000be05fd98
              (s__s__d__internal_codebase_enable_d_ffff0000be0c487e,
               s_aee_encrypt_enable_ffff0000be0c2404,0x157);
    puVar8 = &DAT_ffff0000be0ae542;
  }
  lVar5 = FUN_ffff0000be05df6c(s_mrdump_ffff0000be0bdf63);
  if (lVar5 == 0) {
    pcVar6 = s__s__d__Error__mrdump_partition_i_ffff0000be0b4908;
    uVar7 = 0x15d;
LAB_ffff0000be065968:
    FUN_ffff0000be05fd98(pcVar6,s_aee_encrypt_enable_ffff0000be0c2404,uVar7);
    bVar1 = false;
  }
  else {
    if (*(ulong *)(lVar5 + 0x20) >> 0x1c == 0) {
      FUN_ffff0000be05fd98
                (s__s__d__Warning__mrdump_partition_ffff0000be0b4936,
                 s_aee_encrypt_enable_ffff0000be0c2404,0x163,*(ulong *)(lVar5 + 0x20),0x10000000);
      FUN_ffff0000be05def0(lVar5);
      return false;
    }
    FUN_ffff0000be05def0(lVar5);
    iVar2 = FUN_ffff0000be01fbbc();
    if ((iVar2 != 0) && (lVar3 != 0)) {
      iVar2 = FUN_ffff0000be060cb8(lVar3,&DAT_ffff0000be0ae542);
      if (iVar2 == 0) {
        FUN_ffff0000be05fd98
                  (s__s__d__lk_aee_encrypt_dcfg___yes_ffff0000be0acfa7,
                   s_aee_encrypt_enable_ffff0000be0c2404,0x16c);
        return true;
      }
      iVar2 = FUN_ffff0000be060cb8(lVar3,&DAT_ffff0000be0bac01);
      if (iVar2 == 0) {
        pcVar6 = s__s__d__lk_aee_encrypt_dcfg___no_ffff0000be0a506b;
        uVar7 = 0x16f;
        goto LAB_ffff0000be065968;
      }
    }
    FUN_ffff0000be05fd98
              (s__s__d__aee_encrypt_enable___s_ffff0000be0c1221,
               s_aee_encrypt_enable_ffff0000be0c2404,0x183,puVar8);
    iVar2 = FUN_ffff0000be060cb8(puVar8,&DAT_ffff0000be0ae542);
    bVar1 = iVar2 == 0;
  }
  return bVar1;
}


