
bool FUN_ffff000090783a98(void)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  ulong uVar4;
  long lVar5;
  char *pcVar6;
  undefined8 uVar7;
  undefined *puVar8;
  
  lVar3 = FUN_ffff000090721d30(s_aee_encrypt_ffff0000907dd37d);
  uVar4 = FUN_ffff000090783a88();
  if ((uVar4 & 1) == 0) {
    puVar8 = &DAT_ffff0000907f0cf7;
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d__internal_codebase_enable_d_ffff0000907ffdba,
               s_aee_encrypt_enable_ffff0000907fc521,0x157);
    puVar8 = &DAT_ffff0000907df35d;
  }
  lVar5 = FUN_ffff00009077c028(s_mrdump_ffff0000907f5cf9);
  if (lVar5 == 0) {
    pcVar6 = s__s__d__Error__mrdump_partition_i_ffff0000907e84a6;
    uVar7 = 0x15d;
LAB_ffff000090783b50:
    FUN_ffff00009077e11c(pcVar6,s_aee_encrypt_enable_ffff0000907fc521,uVar7);
    bVar1 = false;
  }
  else {
    if (*(ulong *)(lVar5 + 0x20) >> 0x1c == 0) {
      FUN_ffff00009077e11c
                (s__s__d__Warning__mrdump_partition_ffff0000907e84d4,
                 s_aee_encrypt_enable_ffff0000907fc521,0x163,*(ulong *)(lVar5 + 0x20),0x10000000);
      FUN_ffff00009077bfac(lVar5);
      return false;
    }
    FUN_ffff00009077bfac(lVar5);
    iVar2 = FUN_ffff000090721e60();
    if ((iVar2 != 0) && (lVar3 != 0)) {
      iVar2 = FUN_ffff00009077f0ac(lVar3,&DAT_ffff0000907df35d);
      if (iVar2 == 0) {
        FUN_ffff00009077e11c
                  (s__s__d__lk_aee_encrypt_dcfg___yes_ffff0000907dd389,
                   s_aee_encrypt_enable_ffff0000907fc521,0x16c);
        return true;
      }
      iVar2 = FUN_ffff00009077f0ac(lVar3,&DAT_ffff0000907f0cf7);
      if (iVar2 == 0) {
        pcVar6 = s__s__d__lk_aee_encrypt_dcfg___no_ffff0000907d1e96;
        uVar7 = 0x16f;
        goto LAB_ffff000090783b50;
      }
    }
    FUN_ffff00009077e11c
              (s__s__d__aee_encrypt_enable___s_ffff0000907fa8bd,
               s_aee_encrypt_enable_ffff0000907fc521,0x183,puVar8);
    iVar2 = FUN_ffff00009077f0ac(puVar8,&DAT_ffff0000907df35d);
    bVar1 = iVar2 == 0;
  }
  return bVar1;
}


