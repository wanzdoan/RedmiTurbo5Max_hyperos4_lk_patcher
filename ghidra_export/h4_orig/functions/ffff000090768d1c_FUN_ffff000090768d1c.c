// ffff000090768d1c  size=1404  FUN_ffff000090768d1c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090768d1c(undefined8 param_1,undefined4 param_2,long param_3)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  long lVar4;
  long lVar5;
  long lVar6;
  char *pcVar7;
  long lVar8;
  ulong uVar9;
  long lVar10;
  long lVar11;
  undefined8 uVar12;
  long local_78;
  undefined8 local_6c;
  char local_64 [4];
  
  local_64[0] = '\x01';
  lVar4 = FUN_ffff00009070fd4c(param_2);
  if (param_3 == 0) {
    return 2;
  }
  if (lVar4 == 0) {
    return 2;
  }
  iVar2 = FUN_ffff0000907c47d0(param_3);
  if (iVar2 != 0) {
    return 2;
  }
  lVar5 = FUN_ffff000090721d30(s_bypass_check_ep_ffff0000907fe13e);
  lVar6 = FUN_ffff00009077d514(8);
  FUN_ffff00009077ee50(lVar6,0,8);
  iVar2 = FUN_ffff0000907c50d8(param_3,lVar6);
  if (iVar2 != 0) {
    return 2;
  }
  uVar3 = thunk_FUN_ffff00009074b22c(0x1b);
  uVar3 = uVar3 & 0xe;
  FUN_ffff00009077e11c(s__AVB__sbc_enabled____d_ffff0000907e53d2,uVar3 == 0xe);
  if (uVar3 == 0xe) {
    iVar2 = FUN_ffff00009077eae8(lVar6,&DAT_ffff0000907d8c2f,1);
    if (iVar2 == 0) {
      local_6c = 0;
      FUN_ffff00009077e11c(s__AVB__vbmeta_is_ep_ffff0000907ed0f7);
      iVar2 = FUN_ffff00009074a780(0,(long)&local_6c + 4);
      if (iVar2 != 0) {
        FUN_ffff00009077e11c(s__AVB__Read_efuse_dat0_fail_in_sb_ffff0000907f04f4,iVar2);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c77,0x13b,&DAT_ffff0000907d2506);
      }
      iVar2 = FUN_ffff00009074a780(6,&local_6c);
      if (iVar2 != 0) {
        FUN_ffff00009077e11c(s__AVB__Read_efuse_ctrl0_fail_in_s_ffff0000907eebe0,iVar2);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c77,0x140,&DAT_ffff0000907d2506);
      }
      if ((local_6c._5_1_ == -0x10) && (-1 < (int)local_6c)) {
        FUN_ffff00009077e11c(s__AVB__efuse_is_mp_ffff000090802e78);
        local_6c = local_6c | 0x80000000;
        iVar2 = FUN_ffff00009074ab60(6);
        if (iVar2 != 0) {
          FUN_ffff00009077e11c(s__AVB__EFUSE_IDX_C_CTRL0_blow_fai_ffff0000907d6b67,iVar2);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c77,0x14a,&DAT_ffff0000907d2506)
          ;
        }
      }
    }
  }
  else {
    FUN_ffff00009077e11c(s__AVB__sbc_enabled____d__bypass_c_ffff000090802e4b,0);
  }
  iVar2 = FUN_ffff000090721e60();
  if (((iVar2 == 0) || (lVar5 == 0)) ||
     (iVar2 = FUN_ffff00009077f0ac(lVar5,&DAT_ffff0000907df35d), iVar2 != 0)) {
    if (uVar3 == 0xe) {
      local_6c = local_6c & 0xffffffff;
      iVar2 = FUN_ffff00009074a780(6,(long)&local_6c + 4);
      if (iVar2 != 0) {
        FUN_ffff00009077e11c(s__AVB__Read_efuse_ctrl0_fail_in_s_ffff0000907eebe0,iVar2);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c77,0x15d,&DAT_ffff0000907d2506);
      }
      if ((long)local_6c < 0) {
        FUN_ffff00009077e11c(s__AVB__efuse_is_ep_ffff0000907d6ba0);
        iVar2 = FUN_ffff00009077eae8(lVar6,&DAT_ffff0000907d8c2f,1);
        if (iVar2 != 0) {
          FUN_ffff00009077e11c(s__AVB__vbmeta_mismatched__This_is_ffff0000907ed10b);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c77,0x163,&DAT_ffff0000907d2506)
          ;
        }
        pcVar7 = s__AVB__vbmeta_ep_match__ffff0000907e38e1;
        goto LAB_ffff000090768f34;
      }
    }
  }
  else {
    pcVar7 = s__AVB__bypass_check_ep_rom_ffff0000907dea9a;
LAB_ffff000090768f34:
    FUN_ffff00009077e11c(pcVar7);
  }
  if (lVar6 != 0) {
    thunk_FUN_ffff0000907c314c(lVar6);
  }
  FUN_ffff00009077e11c(s__AVB__vbmeta_ep_check_end__ffff0000907eec12);
  lVar5 = FUN_ffff000090721d30(s_bypass_check_imgkey_ffff0000907d3e26);
  uVar3 = thunk_FUN_ffff00009074b22c(0x1b);
  local_6c = CONCAT44(uVar3,(int)local_6c);
  iVar2 = FUN_ffff000090721e60();
  if (((iVar2 == 0) || (lVar5 == 0)) ||
     (iVar2 = FUN_ffff00009077f0ac(lVar5,&DAT_ffff0000907df35d), iVar2 != 0)) {
    if ((uVar3 & 0xe) == 0xe) {
      _DAT_ffff00009087cd00 = FUN_ffff0000907c4f6c(param_3);
      FUN_ffff00009077e11c(s__AVB__sku_str____s_ffff0000907f540e,_DAT_ffff00009087cd00);
      iVar2 = FUN_ffff00009074a780(0,(long)&local_6c + 4);
      if (iVar2 != 0) {
        FUN_ffff00009077e11c(s__AVB__Read_efuse_fail_in_sboot__0_ffff0000907e0255,iVar2);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c77,0x18b,&DAT_ffff0000907d2506);
      }
      if (local_6c._4_1_ == '\0') {
        FUN_ffff00009077e11c(s__AVB_efuse_sku___CN_ffff0000907ffc63);
        uVar12 = _DAT_ffff00009087cd00;
        iVar2 = FUN_ffff00009077eae8(_DAT_ffff00009087cd00,&DAT_ffff0000907d36ee,2);
        if ((iVar2 != 0) &&
           (iVar2 = FUN_ffff00009077eae8(uVar12,s_NATIVE_ffff0000907d36f1,6), iVar2 != 0)) {
          FUN_ffff00009077e11c(s__AVB_vbmeta_sku_mismatch__This_i_ffff000090801646);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c77,0x191,&DAT_ffff0000907d2506)
          ;
        }
        pcVar7 = s__AVB_vbmeta_sku_match__ffff0000907e38f9;
      }
      else {
        FUN_ffff00009077e11c(s__AVB_efuse_sku___non_CN_ffff0000907eb796);
        iVar2 = FUN_ffff00009077eae8(_DAT_ffff00009087cd00,&DAT_ffff0000907d36ee,2);
        if (iVar2 != 0) goto LAB_ffff00009076905c;
        pcVar7 = s__AVB_vbmeta_sku_mismatch__GL_dev_ffff0000907d1c97;
      }
      goto LAB_ffff000090769058;
    }
  }
  else {
    pcVar7 = s__AVB__bypass_check_region_img_ke_ffff0000907e53ea;
LAB_ffff000090769058:
    FUN_ffff00009077e11c(pcVar7);
  }
LAB_ffff00009076905c:
  if (*(long *)(param_3 + 0x20) == 0) {
    FUN_ffff000090710aec();
    return 8;
  }
  uVar9 = 0;
  local_78 = 0;
  lVar5 = 0;
  lVar6 = 0;
  do {
    lVar8 = lVar5;
    lVar10 = lVar6;
    lVar1 = local_78;
    if (*(long *)(*(long *)(param_3 + 0x18) + uVar9 * 0x20) != 0) {
      lVar11 = *(long *)(*(long *)(param_3 + 0x18) + uVar9 * 0x20 + 8);
      iVar2 = FUN_ffff00009070fd78(lVar11);
      if (iVar2 == 0) {
        uVar12 = *(undefined8 *)(*(long *)(param_3 + 0x18) + uVar9 * 0x20);
        iVar2 = FUN_ffff00009077f0ac(lVar4,uVar12);
        lVar10 = lVar11;
        if (((iVar2 != 0) &&
            (iVar2 = FUN_ffff00009077f0ac(s_init_boot_ffff0000907f2652,uVar12), lVar10 = lVar6,
            lVar1 = lVar11, iVar2 != 0)) &&
           (iVar2 = FUN_ffff00009077f0ac(s_vendor_boot_ffff0000907e24d3,uVar12), lVar8 = lVar11,
           lVar1 = local_78, iVar2 != 0)) {
          lVar8 = lVar5;
        }
      }
    }
    local_78 = lVar1;
    uVar9 = (ulong)((int)uVar9 + 1);
    lVar5 = lVar8;
    lVar6 = lVar10;
  } while (uVar9 < *(ulong *)(param_3 + 0x20));
  FUN_ffff000090710aec();
  if (lVar10 == 0) {
    return 8;
  }
  FUN_ffff000090719078(s_root__dev_ram_ffff0000907fe14e);
  lVar4 = FUN_ffff00009071f790();
  if (lVar4 == 0) {
    FUN_ffff00009077e11c(s__AVB__Fail_to_get_suffix_ffff0000907e540d);
    return 8;
  }
  uVar9 = FUN_ffff0000907c2d68(param_1,lVar4,local_64);
  if ((uVar9 & 1) == 0) {
    FUN_ffff00009077e11c(s__AVB__Fail_To_Get_Verification_D_ffff0000907e3911);
    local_64[0] = '\x01';
  }
  else if (local_64[0] == '\0') goto LAB_ffff000090769184;
  uVar12 = FUN_ffff0000907694b4(*(undefined8 *)(param_3 + 0x28));
  if ((int)uVar12 != 0) {
    return uVar12;
  }
LAB_ffff000090769184:
  FUN_ffff000090719078(*(undefined8 *)(param_3 + 0x28));
  FUN_ffff000090710a40(lVar10,lVar8,local_78);
  uVar12 = FUN_ffff0000907c62f4(param_1,s_seccfg_ffff0000907d1c70);
  return uVar12;
}


