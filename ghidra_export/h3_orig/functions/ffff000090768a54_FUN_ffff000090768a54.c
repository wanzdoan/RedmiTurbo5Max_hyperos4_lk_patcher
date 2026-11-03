// ffff000090768a54  size=1404  FUN_ffff000090768a54

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090768a54(undefined8 param_1,undefined4 param_2,long param_3)

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
  lVar4 = FUN_ffff00009070fcd4(param_2);
  if (param_3 == 0) {
    return 2;
  }
  if (lVar4 == 0) {
    return 2;
  }
  iVar2 = FUN_ffff0000907c415c(param_3);
  if (iVar2 != 0) {
    return 2;
  }
  lVar5 = FUN_ffff000090721af0(s_bypass_check_ep_ffff0000907fdf96);
  lVar6 = FUN_ffff00009077d24c(8);
  FUN_ffff00009077eb88(lVar6,0,8);
  iVar2 = FUN_ffff0000907c4a64(param_3,lVar6);
  if (iVar2 != 0) {
    return 2;
  }
  uVar3 = thunk_FUN_ffff00009074af94(0x1b);
  uVar3 = uVar3 & 0xe;
  FUN_ffff00009077de54(s__AVB__sbc_enabled____d_ffff0000907e52ce,uVar3 == 0xe);
  if (uVar3 == 0xe) {
    iVar2 = FUN_ffff00009077e820(lVar6,&DAT_ffff0000907d8bae,1);
    if (iVar2 == 0) {
      local_6c = 0;
      FUN_ffff00009077de54(s__AVB__vbmeta_is_ep_ffff0000907ecf74);
      iVar2 = FUN_ffff00009074a4e8(0,(long)&local_6c + 4);
      if (iVar2 != 0) {
        FUN_ffff00009077de54(s__AVB__Read_efuse_dat0_fail_in_sb_ffff0000907f0383,iVar2);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c3a,0x13b,&DAT_ffff0000907d24c9);
      }
      iVar2 = FUN_ffff00009074a4e8(6,&local_6c);
      if (iVar2 != 0) {
        FUN_ffff00009077de54(s__AVB__Read_efuse_ctrl0_fail_in_s_ffff0000907eea6f,iVar2);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c3a,0x140,&DAT_ffff0000907d24c9);
      }
      if ((local_6c._5_1_ == -0x10) && (-1 < (int)local_6c)) {
        FUN_ffff00009077de54(s__AVB__efuse_is_mp_ffff000090802c74);
        local_6c = local_6c | 0x80000000;
        iVar2 = FUN_ffff00009074a8c8(6);
        if (iVar2 != 0) {
          FUN_ffff00009077de54(s__AVB__EFUSE_IDX_C_CTRL0_blow_fai_ffff0000907d6ad1,iVar2);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cd04
                    (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c3a,0x14a,&DAT_ffff0000907d24c9)
          ;
        }
      }
    }
  }
  else {
    FUN_ffff00009077de54(s__AVB__sbc_enabled____d__bypass_c_ffff000090802c47,0);
  }
  iVar2 = FUN_ffff000090721c20();
  if (((iVar2 == 0) || (lVar5 == 0)) ||
     (iVar2 = FUN_ffff00009077ede4(lVar5,&DAT_ffff0000907df2ba), iVar2 != 0)) {
    if (uVar3 == 0xe) {
      local_6c = local_6c & 0xffffffff;
      iVar2 = FUN_ffff00009074a4e8(6,(long)&local_6c + 4);
      if (iVar2 != 0) {
        FUN_ffff00009077de54(s__AVB__Read_efuse_ctrl0_fail_in_s_ffff0000907eea6f,iVar2);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c3a,0x15d,&DAT_ffff0000907d24c9);
      }
      if ((long)local_6c < 0) {
        FUN_ffff00009077de54(s__AVB__efuse_is_ep_ffff0000907d6b0a);
        iVar2 = FUN_ffff00009077e820(lVar6,&DAT_ffff0000907d8bae,1);
        if (iVar2 != 0) {
          FUN_ffff00009077de54(s__AVB__vbmeta_mismatched__This_is_ffff0000907ecf88);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cd04
                    (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c3a,0x163,&DAT_ffff0000907d24c9)
          ;
        }
        pcVar7 = s__AVB__vbmeta_ep_match__ffff0000907e3810;
        goto LAB_ffff000090768c6c;
      }
    }
  }
  else {
    pcVar7 = s__AVB__bypass_check_ep_rom_ffff0000907de9f7;
LAB_ffff000090768c6c:
    FUN_ffff00009077de54(pcVar7);
  }
  if (lVar6 != 0) {
    thunk_FUN_ffff0000907c2ad8(lVar6);
  }
  FUN_ffff00009077de54(s__AVB__vbmeta_ep_check_end__ffff0000907eeaa1);
  lVar5 = FUN_ffff000090721af0(s_bypass_check_imgkey_ffff0000907d3de9);
  uVar3 = thunk_FUN_ffff00009074af94(0x1b);
  local_6c = CONCAT44(uVar3,(int)local_6c);
  iVar2 = FUN_ffff000090721c20();
  if (((iVar2 == 0) || (lVar5 == 0)) ||
     (iVar2 = FUN_ffff00009077ede4(lVar5,&DAT_ffff0000907df2ba), iVar2 != 0)) {
    if ((uVar3 & 0xe) == 0xe) {
      _DAT_ffff00009087cc68 = FUN_ffff0000907c48f8(param_3);
      FUN_ffff00009077de54(s__AVB__sku_str____s_ffff0000907f52a1,_DAT_ffff00009087cc68);
      iVar2 = FUN_ffff00009074a4e8(0,(long)&local_6c + 4);
      if (iVar2 != 0) {
        FUN_ffff00009077de54(s__AVB__Read_efuse_fail_in_sboot__0_ffff0000907e01b2,iVar2);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c3a,0x18b,&DAT_ffff0000907d24c9);
      }
      if (local_6c._4_1_ == '\0') {
        FUN_ffff00009077de54(s__AVB_efuse_sku___CN_ffff0000907ffabb);
        uVar12 = _DAT_ffff00009087cc68;
        iVar2 = FUN_ffff00009077e820(_DAT_ffff00009087cc68,&DAT_ffff0000907d36b1,2);
        if ((iVar2 != 0) &&
           (iVar2 = FUN_ffff00009077e820(uVar12,s_NATIVE_ffff0000907d36b4,6), iVar2 != 0)) {
          FUN_ffff00009077de54(s__AVB_vbmeta_sku_mismatch__This_i_ffff00009080146a);
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cd04
                    (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c3a,0x191,&DAT_ffff0000907d24c9)
          ;
        }
        pcVar7 = s__AVB_vbmeta_sku_match__ffff0000907e3828;
      }
      else {
        FUN_ffff00009077de54(s__AVB_efuse_sku___non_CN_ffff0000907eb613);
        iVar2 = FUN_ffff00009077e820(_DAT_ffff00009087cc68,&DAT_ffff0000907d36b1,2);
        if (iVar2 != 0) goto LAB_ffff000090768d94;
        pcVar7 = s__AVB_vbmeta_sku_mismatch__GL_dev_ffff0000907d1c5a;
      }
      goto LAB_ffff000090768d90;
    }
  }
  else {
    pcVar7 = s__AVB__bypass_check_region_img_ke_ffff0000907e52e6;
LAB_ffff000090768d90:
    FUN_ffff00009077de54(pcVar7);
  }
LAB_ffff000090768d94:
  if (*(long *)(param_3 + 0x20) == 0) {
    FUN_ffff000090710a74();
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
      iVar2 = FUN_ffff00009070fd00(lVar11);
      if (iVar2 == 0) {
        uVar12 = *(undefined8 *)(*(long *)(param_3 + 0x18) + uVar9 * 0x20);
        iVar2 = FUN_ffff00009077ede4(lVar4,uVar12);
        lVar10 = lVar11;
        if (((iVar2 != 0) &&
            (iVar2 = FUN_ffff00009077ede4(s_init_boot_ffff0000907f24c6,uVar12), lVar10 = lVar6,
            lVar1 = lVar11, iVar2 != 0)) &&
           (iVar2 = FUN_ffff00009077ede4(s_vendor_boot_ffff0000907e2402,uVar12), lVar8 = lVar11,
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
  FUN_ffff000090710a74();
  if (lVar10 == 0) {
    return 8;
  }
  FUN_ffff000090718f34(s_root__dev_ram_ffff0000907fdfa6);
  lVar4 = FUN_ffff00009071f608();
  if (lVar4 == 0) {
    FUN_ffff00009077de54(s__AVB__Fail_to_get_suffix_ffff0000907e5309);
    return 8;
  }
  uVar9 = FUN_ffff0000907c26f4(param_1,lVar4,local_64);
  if ((uVar9 & 1) == 0) {
    FUN_ffff00009077de54(s__AVB__Fail_To_Get_Verification_D_ffff0000907e3840);
    local_64[0] = '\x01';
  }
  else if (local_64[0] == '\0') goto LAB_ffff000090768ebc;
  uVar12 = FUN_ffff0000907691ec(*(undefined8 *)(param_3 + 0x28));
  if ((int)uVar12 != 0) {
    return uVar12;
  }
LAB_ffff000090768ebc:
  FUN_ffff000090718f34(*(undefined8 *)(param_3 + 0x28));
  FUN_ffff0000907109c8(lVar10,lVar8,local_78);
  uVar12 = FUN_ffff0000907c5c80(param_1,s_seccfg_ffff0000907d1c33);
  return uVar12;
}


