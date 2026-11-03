// ffff000090723550  size=1108  FUN_ffff000090723550

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090723550(long param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  byte bVar4;
  ushort uVar5;
  ushort uVar6;
  int iVar7;
  int iVar8;
  uint uVar9;
  int iVar10;
  uint uVar11;
  undefined8 local_b8;
  undefined8 local_b0;
  undefined8 local_a8;
  undefined8 local_a0;
  undefined8 local_98;
  uint local_90;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 local_78;
  int local_70;
  int iStack_6c;
  undefined8 local_68;
  int local_60;
  int iStack_5c;
  int local_58;
  int iStack_54;
  int local_38;
  int iStack_34;
  
  if (param_1 != 0) {
    iVar7 = FUN_ffff00009076da24(param_1,s__dramc_ffff00009080082b);
    if ((iVar7 < 0) &&
       (iVar7 = FUN_ffff00009076da24(param_1,s__soc_dramc_ffff0000907e0f85), iVar7 < 0)) {
      FUN_ffff00009077e11c
                (s___s__get_path_offset_fail_ffff0000907f9829,s_set_fdt_dramc_ffff0000907e2932);
      return;
    }
    uVar6 = _DAT_ffff000090860716;
    uVar5 = _DAT_ffff000090860714;
    bVar4 = DAT_ffff000090860713;
    iStack_34 = (uint)DAT_ffff000090860710 << 0x18;
    local_38 = (uint)DAT_ffff000090860711 << 0x18;
    iStack_54 = (uint)DAT_ffff000090860712 << 0x18;
    local_68 = 0;
    local_58 = (uint)DAT_ffff000090860713 << 0x18;
    iStack_5c = (uint)(ushort)(_DAT_ffff000090860714 >> 8 | _DAT_ffff000090860714 << 8) << 0x10;
    local_60 = (uint)(ushort)(_DAT_ffff000090860716 >> 8 | _DAT_ffff000090860716 << 8) << 0x10;
    FUN_ffff00009077e11c
              (s__s__d__offset____d_ffff0000907e5f94,s_set_fdt_dramc_ffff0000907e2932,0x49,iVar7);
    FUN_ffff00009077e11c
              (s__s__d__dram_type_original_data___ffff000090802347,s_set_fdt_dramc_ffff0000907e2932,
               0x4a,DAT_ffff000090860710);
    if (bVar4 == 0) {
      uVar9 = 0;
      uVar11 = 0;
    }
    else {
      uVar11 = (uint)(_DAT_ffff000090860718 >> 0x1b);
      uVar11 = (uVar11 & 0xff00ff00) >> 8 | (uVar11 & 0xff00ff) << 8;
      uVar11 = uVar11 >> 0x10 | uVar11 << 0x10;
      uVar9 = (uint)(_DAT_ffff000090860720 >> 0x1b);
      uVar9 = (uVar9 & 0xff00ff00) >> 8 | (uVar9 & 0xff00ff) << 8;
      uVar9 = uVar9 >> 0x10 | uVar9 << 0x10;
    }
    local_68 = CONCAT44(uVar9,uVar11);
    if (uVar5 == 0) {
      iVar8 = 0;
      iVar10 = 0;
      local_88 = 0;
      local_80 = 0;
      local_78 = 0;
    }
    else {
      local_88 = CONCAT44(((uint)(_DAT_ffff000090860756 >> 8) |
                          (_DAT_ffff000090860756 & 0xff00ff) << 8) << 0x10,
                          ((uint)(_DAT_ffff000090860754 >> 8) |
                          (_DAT_ffff000090860754 & 0xff00ff) << 8) << 0x10);
      local_80 = CONCAT44(((uint)(_DAT_ffff00009086075a >> 8) |
                          (_DAT_ffff00009086075a & 0xff00ff) << 8) << 0x10,
                          ((uint)(_DAT_ffff000090860758 >> 8) |
                          (_DAT_ffff000090860758 & 0xff00ff) << 8) << 0x10);
      iVar8 = ((uint)(_DAT_ffff000090860760 >> 8) | (_DAT_ffff000090860760 & 0xff00ff) << 8) << 0x10
      ;
      iVar10 = ((uint)(_DAT_ffff000090860762 >> 8) | (_DAT_ffff000090860762 & 0xff00ff) << 8) <<
               0x10;
      local_78 = CONCAT44(((uint)(_DAT_ffff00009086075e >> 8) |
                          (_DAT_ffff00009086075e & 0xff00ff) << 8) << 0x10,
                          ((uint)(_DAT_ffff00009086075c >> 8) |
                          (_DAT_ffff00009086075c & 0xff00ff) << 8) << 0x10);
    }
    _local_70 = CONCAT44(iVar10,iVar8);
    if (uVar6 == 0) {
      local_90 = 0;
      local_b8 = 0;
      local_b0 = 0;
      local_a8 = 0;
      local_a0 = 0;
      local_98 = 0;
    }
    else {
      uVar11 = (_DAT_ffff000090860728 & 0xff00ff00) >> 8 | (_DAT_ffff000090860728 & 0xff00ff) << 8;
      uVar9 = (_DAT_ffff00009086072c & 0xff00ff00) >> 8 | (_DAT_ffff00009086072c & 0xff00ff) << 8;
      uVar1 = (_DAT_ffff000090860730 & 0xff00ff00) >> 8 | (_DAT_ffff000090860730 & 0xff00ff) << 8;
      uVar2 = (_DAT_ffff000090860734 & 0xff00ff00) >> 8 | (_DAT_ffff000090860734 & 0xff00ff) << 8;
      local_b8 = CONCAT44(uVar9 >> 0x10 | uVar9 << 0x10,uVar11 >> 0x10 | uVar11 << 0x10);
      local_b0 = CONCAT44(uVar2 >> 0x10 | uVar2 << 0x10,uVar1 >> 0x10 | uVar1 << 0x10);
      uVar11 = (_DAT_ffff00009086073c & 0xff00ff00) >> 8 | (_DAT_ffff00009086073c & 0xff00ff) << 8;
      uVar9 = (_DAT_ffff000090860738 & 0xff00ff00) >> 8 | (_DAT_ffff000090860738 & 0xff00ff) << 8;
      uVar1 = (_DAT_ffff000090860740 & 0xff00ff00) >> 8 | (_DAT_ffff000090860740 & 0xff00ff) << 8;
      uVar2 = (_DAT_ffff000090860744 & 0xff00ff00) >> 8 | (_DAT_ffff000090860744 & 0xff00ff) << 8;
      local_a8 = CONCAT44(uVar11 >> 0x10 | uVar11 << 0x10,uVar9 >> 0x10 | uVar9 << 0x10);
      uVar11 = (_DAT_ffff000090860748 & 0xff00ff00) >> 8 | (_DAT_ffff000090860748 & 0xff00ff) << 8;
      uVar9 = (_DAT_ffff00009086074c & 0xff00ff00) >> 8 | (_DAT_ffff00009086074c & 0xff00ff) << 8;
      uVar3 = (_DAT_ffff000090860750 & 0xff00ff00) >> 8 | (_DAT_ffff000090860750 & 0xff00ff) << 8;
      local_90 = uVar3 >> 0x10 | uVar3 << 0x10;
      local_a0 = CONCAT44(uVar2 >> 0x10 | uVar2 << 0x10,uVar1 >> 0x10 | uVar1 << 0x10);
      local_98 = CONCAT44(uVar9 >> 0x10 | uVar9 << 0x10,uVar11 >> 0x10 | uVar11 << 0x10);
    }
    iVar8 = FUN_ffff00009076e6d4(param_1,iVar7,s_dram_type_ffff0000907d0d69,&iStack_34,4);
    if (iVar8 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_dramc_d_ffff0000907fb50e,0x6d,&DAT_ffff0000907d2506);
    }
    iVar8 = FUN_ffff00009076e6d4(param_1,iVar7,s_support_ch_cnt_ffff0000907e0f90,&local_38,4);
    if (iVar8 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_dramc_d_ffff0000907fb50e,0x71,&DAT_ffff0000907d2506);
    }
    iVar8 = FUN_ffff00009076e6d4(param_1,iVar7,s_ch_cnt_ffff0000907f6173,&iStack_54,4);
    if (iVar8 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_dramc_d_ffff0000907fb50e,0x75,&DAT_ffff0000907d2506);
    }
    iVar8 = FUN_ffff00009076e6d4(param_1,iVar7,s_rk_cnt_ffff0000907d777e,&local_58,4);
    if (iVar8 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_dramc_d_ffff0000907fb50e,0x79,&DAT_ffff0000907d2506);
    }
    iVar8 = FUN_ffff00009076e6d4(param_1,iVar7,s_mr_cnt_ffff0000907d9050,&iStack_5c,4);
    if (iVar8 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_dramc_d_ffff0000907fb50e,0x7d,&DAT_ffff0000907d2506);
    }
    iVar8 = FUN_ffff00009076e6d4(param_1,iVar7,s_freq_cnt_ffff0000907dc532,&local_60,4);
    if (iVar8 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_dramc_d_ffff0000907fb50e,0x81,&DAT_ffff0000907d2506);
    }
    iVar8 = FUN_ffff00009076e6d4(param_1,iVar7,s_rk_size_ffff0000907f9844,&local_68,8);
    if (iVar8 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_dramc_d_ffff0000907fb50e,0x85,&DAT_ffff0000907d2506);
    }
    iVar8 = FUN_ffff00009076e6d4(param_1,iVar7,&DAT_ffff0000907f454e,&local_88,0x20);
    if (iVar8 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_dramc_d_ffff0000907fb50e,0x89,&DAT_ffff0000907d2506);
    }
    iVar7 = FUN_ffff00009076e6d4(param_1,iVar7,s_freq_step_ffff0000907fee07,&local_b8,0x2c);
    if (iVar7 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_dramc_d_ffff0000907fb50e,0x8d,&DAT_ffff0000907d2506);
    }
  }
  return;
}


