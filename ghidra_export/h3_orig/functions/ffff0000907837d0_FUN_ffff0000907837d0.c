// ffff0000907837d0  size=384  FUN_ffff0000907837d0

bool FUN_ffff0000907837d0(void)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  ulong uVar4;
  long lVar5;
  char *pcVar6;
  undefined8 uVar7;
  undefined *puVar8;
  
  lVar3 = FUN_ffff000090721af0(s_aee_encrypt_ffff0000907dd2a7);
  uVar4 = FUN_ffff0000907837c0();
  if ((uVar4 & 1) == 0) {
    puVar8 = &DAT_ffff0000907f0b86;
  }
  else {
    FUN_ffff00009077de54
              (s__s__d__internal_codebase_enable_d_ffff0000907ffc12,
               s_aee_encrypt_enable_ffff0000907fc385,0x157);
    puVar8 = &DAT_ffff0000907df2ba;
  }
  lVar5 = FUN_ffff00009077bd60(s_mrdump_ffff0000907f5b82);
  if (lVar5 == 0) {
    pcVar6 = s__s__d__Error__mrdump_partition_i_ffff0000907e8362;
    uVar7 = 0x15d;
LAB_ffff000090783888:
    FUN_ffff00009077de54(pcVar6,s_aee_encrypt_enable_ffff0000907fc385,uVar7);
    bVar1 = false;
  }
  else {
    if (*(ulong *)(lVar5 + 0x20) >> 0x1c == 0) {
      FUN_ffff00009077de54
                (s__s__d__Warning__mrdump_partition_ffff0000907e8390,
                 s_aee_encrypt_enable_ffff0000907fc385,0x163,*(ulong *)(lVar5 + 0x20),0x10000000);
      FUN_ffff00009077bce4(lVar5);
      return false;
    }
    FUN_ffff00009077bce4(lVar5);
    iVar2 = FUN_ffff000090721c20();
    if ((iVar2 != 0) && (lVar3 != 0)) {
      iVar2 = FUN_ffff00009077ede4(lVar3,&DAT_ffff0000907df2ba);
      if (iVar2 == 0) {
        FUN_ffff00009077de54
                  (s__s__d__lk_aee_encrypt_dcfg___yes_ffff0000907dd2b3,
                   s_aee_encrypt_enable_ffff0000907fc385,0x16c);
        return true;
      }
      iVar2 = FUN_ffff00009077ede4(lVar3,&DAT_ffff0000907f0b86);
      if (iVar2 == 0) {
        pcVar6 = s__s__d__lk_aee_encrypt_dcfg___no_ffff0000907d1e59;
        uVar7 = 0x16f;
        goto LAB_ffff000090783888;
      }
    }
    FUN_ffff00009077de54
              (s__s__d__aee_encrypt_enable___s_ffff0000907fa721,
               s_aee_encrypt_enable_ffff0000907fc385,0x183,puVar8);
    iVar2 = FUN_ffff00009077ede4(puVar8,&DAT_ffff0000907df2ba);
    bVar1 = iVar2 == 0;
  }
  return bVar1;
}


