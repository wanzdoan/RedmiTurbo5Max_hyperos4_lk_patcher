
int FUN_ffff000090769824(undefined8 param_1,undefined4 param_2,long param_3)

{
  long lVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  long lVar5;
  ulong uVar6;
  long lVar7;
  long lVar8;
  char *pcVar9;
  undefined8 uVar10;
  long lVar11;
  undefined8 uVar12;
  long local_70;
  char local_64 [4];

  local_64[0] = '\x01';
  lVar3 = FUN_ffff00009070f894(param_2);
  if (param_3 == 0) {
    return 2;
  }
  if (lVar3 == 0) {
    return 2;
  }
  iVar2 = FUN_ffff0000907c4818(param_3);
  if (iVar2 != 0) {
    return 2;
  }
  FUN_ffff00009077ea84(s__AVB__bypass_check_ep_rom_in_fac_ffff0000907fa504);
  FUN_ffff00009077ea84(s__AVB__bypass_check_region_img_ke_ffff0000907df31e);
  if (*(long *)(param_3 + 0x20) == 0) {
    FUN_ffff000090710634();
    return 8;
  }
  uVar6 = 0;
  local_70 = 0;
  lVar5 = 0;
  lVar8 = 0;
  do {
    lVar4 = lVar5;
    lVar7 = lVar8;
    lVar1 = local_70;
    if (*(long *)(*(long *)(param_3 + 0x18) + uVar6 * 0x20) != 0) {
      lVar11 = *(long *)(*(long *)(param_3 + 0x18) + uVar6 * 0x20 + 8);
      iVar2 = FUN_ffff00009070f8c0(lVar11);
      if (iVar2 == 0) {
        uVar12 = *(undefined8 *)(*(long *)(param_3 + 0x18) + uVar6 * 0x20);
        iVar2 = FUN_ffff00009077f990(lVar3,uVar12);
        lVar7 = lVar11;
        if (((iVar2 != 0) &&
            (iVar2 = FUN_ffff00009077f990(s_init_boot_ffff0000907eb20c,uVar12), lVar7 = lVar8,
            lVar1 = lVar11, iVar2 != 0)) &&
           (iVar2 = FUN_ffff00009077f990(s_vendor_boot_ffff0000907dabe7,uVar12), lVar4 = lVar11,
           lVar1 = local_70, iVar2 != 0)) {
          lVar4 = lVar5;
        }
      }
    }
    local_70 = lVar1;
    uVar6 = (ulong)((int)uVar6 + 1);
    lVar5 = lVar4;
    lVar8 = lVar7;
  } while (uVar6 < *(ulong *)(param_3 + 0x20));
  FUN_ffff000090710634();
  if (lVar7 == 0) {
    return 8;
  }
  FUN_ffff000090718ab8(s_root__dev_ram_ffff0000907f6f0f);
  lVar3 = FUN_ffff00009071f2b0();
  if (lVar3 == 0) {
    FUN_ffff00009077ea84(s__AVB__Fail_to_get_suffix_ffff0000907ddabf);
    return 8;
  }
  uVar6 = FUN_ffff0000907c2d28(param_1,lVar3,local_64);
  if ((uVar6 & 1) == 0) {
    FUN_ffff00009077ea84(s__AVB__Fail_To_Get_Verification_D_ffff0000907dc011);
    local_64[0] = '\x01';
  }
  else if (local_64[0] == '\0') goto LAB_ffff000090769a38;
  uVar10 = *(undefined8 *)(param_3 + 0x28);
  uVar12 = FUN_ffff00009077fb34(uVar10);
  pcVar9 = s_androidboot_vbmeta_hash_alg_ffff0000907e27c1;
  iVar2 = FUN_ffff0000907c5d28(uVar10,uVar12,s_androidboot_vbmeta_hash_alg_ffff0000907e27c1);
  if (iVar2 == 0) {
    uVar12 = FUN_ffff00009077fb34(uVar10);
    pcVar9 = s_androidboot_vbmeta_size_ffff0000907f6f1d;
    iVar2 = FUN_ffff0000907c5d28(uVar10,uVar12,s_androidboot_vbmeta_size_ffff0000907f6f1d);
    if (iVar2 == 0) {
      uVar12 = FUN_ffff00009077fb34(uVar10);
      pcVar9 = s_androidboot_vbmeta_digest_ffff0000907e27dd;
      iVar2 = FUN_ffff0000907c5d28(uVar10,uVar12,s_androidboot_vbmeta_digest_ffff0000907e27dd);
      if (iVar2 == 0) {
LAB_ffff000090769a38:
        FUN_ffff00009077ea84(s__AVB__cmdline____s_ffff0000907f8aa0,*(undefined8 *)(param_3 + 0x28));
        FUN_ffff000090718ab8(*(undefined8 *)(param_3 + 0x28));
        FUN_ffff000090710588(lVar7,lVar4,local_70);
        iVar2 = FUN_ffff0000907c6154(param_1,s_seccfg_ffff0000907c9cbd);
        return iVar2;
      }
    }
  }
  FUN_ffff00009077ea84(s__AVB__Extract__s_fails_ffff0000907d07f3,pcVar9);
  return iVar2;
}
