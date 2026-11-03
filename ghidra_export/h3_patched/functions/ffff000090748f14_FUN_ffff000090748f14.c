// ffff000090748f14  size=1844  FUN_ffff000090748f14

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090748f14(long param_1)

{
  int iVar1;
  uint *puVar2;
  int *piVar3;
  char *pcVar4;
  undefined *puVar5;
  undefined4 uVar6;
  undefined1 *puVar7;
  undefined4 uVar8;
  undefined4 uVar9;
  uint uVar10;
  undefined4 *puVar11;
  undefined4 local_38;
  undefined4 local_34;
  
  if (DAT_ffff000090877668 == '\x01') {
    FUN_ffff00009077de54
              (s__DCM__ERROR___s__failed_due_to_i_ffff0000907d1568,s_mt_dcm_init_ffff0000907ec80f);
    return 0;
  }
  local_38 = 0;
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18(s_lk_driver_fdt_is_NULL__ffff0000907fd794);
  }
  iVar1 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_mt6991_dcm_ffff0000907d6487);
  if (iVar1 < 0) {
    pcVar4 = s__DCM__WARN___DCM__mediatek_dcm_n_ffff0000907dca5d;
LAB_ffff000090748fb4:
    FUN_ffff00009077de54(pcVar4);
    uVar10 = 0;
  }
  else {
    puVar2 = (uint *)FUN_ffff00009076dcd4(param_1,iVar1,s_mcu_disable_ffff0000907e30ce,&local_38);
    if (puVar2 == (uint *)0x0) {
      pcVar4 = s__DCM__WARN___DCM__get_property_i_ffff0000907ee3c9;
      goto LAB_ffff000090748fb4;
    }
    uVar10 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
    uVar10 = uVar10 >> 0x10 | uVar10 << 0x10;
  }
  local_34 = 0;
  iVar1 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_mt6991_dcm_ffff0000907d6487);
  if (iVar1 < 0) {
    FUN_ffff00009077de54(s__DCM__WARN___DCM__mediatek_dcm_n_ffff0000907dca5d);
joined_r0xffff000090749020:
    if (uVar10 == 0) goto LAB_ffff000090749050;
    uVar9 = 0x4002;
LAB_ffff00009074903c:
    pcVar4 = s__DCM__WARN___DCM__MCU_DCM_is_dis_ffff0000907d2fb5;
  }
  else {
    piVar3 = (int *)FUN_ffff00009076dcd4(param_1,iVar1,s_infra_disable_ffff0000907db014,&local_34);
    if (piVar3 == (int *)0x0) {
      FUN_ffff00009077de54(s__DCM__WARN___DCM__get_property_i_ffff0000907ee3c9);
      goto joined_r0xffff000090749020;
    }
    if ((uVar10 != 0) && (*piVar3 != 0)) {
      FUN_ffff00009077de54(s__DCM__WARN___DCM__Infra_DCM_is_d_ffff0000907fd764);
      uVar9 = 0x4003;
      goto LAB_ffff00009074903c;
    }
    pcVar4 = s__DCM__WARN___DCM__MCU_DCM_is_dis_ffff0000907d2fb5;
    if (uVar10 == 0) {
      pcVar4 = s__DCM__WARN___DCM__Infra_DCM_is_d_ffff0000907fd764;
    }
    uVar9 = 0x4002;
    if (uVar10 == 0) {
      uVar9 = 1;
    }
    if (uVar10 == 0 && *piVar3 == 0) goto LAB_ffff000090749050;
  }
  FUN_ffff00009077de54(pcVar4);
  FUN_ffff000090749648(uVar9);
LAB_ffff000090749050:
  uVar10 = DAT_ffff00009081a72c;
  FUN_ffff00009077de54
            (s__DCM__WARN____s_type_init_dcm_ty_ffff000090800d67,s_dcm_set_default_ffff000090800daa,
             DAT_ffff00009081a72c,0);
  if (PTR_s_INFRA_DCM_ffff00009081a760 != (undefined *)0x0) {
    puVar5 = PTR_s_INFRA_DCM_ffff00009081a760;
    puVar11 = &DAT_ffff00009081a730;
    do {
      if ((puVar11[10] & uVar10) != 0) {
        uVar9 = puVar11[3];
        puVar11[2] = 0;
        *puVar11 = uVar9;
        puVar11[1] = uVar9;
        if (puVar11[10] == 0) {
          uVar8 = 0;
          uVar6 = 0;
        }
        else {
          if (*(code **)(puVar11 + 8) != (code *)0x0) {
            (**(code **)(puVar11 + 8))();
            uVar9 = *puVar11;
          }
          (**(code **)(puVar11 + 4))(uVar9);
          puVar5 = *(undefined **)(puVar11 + 0xc);
          uVar6 = puVar11[10];
          uVar9 = *puVar11;
          uVar8 = puVar11[2];
        }
        FUN_ffff00009077de54
                  (s__DCM__INFO____s_0x_X__current_st_ffff0000907dfc4e,puVar5,uVar6,uVar9,uVar8);
      }
      puVar5 = *(undefined **)(puVar11 + 0x1a);
      puVar11 = puVar11 + 0xe;
    } while (puVar5 != (undefined *)0x0);
  }
  FUN_ffff00009077de54(s__DCM__INFO___________dcm_dump_re_ffff0000907e3117);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_CLK_AXI_VDNR_DCM_TOP_APINFRA_IO__ffff0000907f84ea,&DAT_ffff000010156008,
             _DAT_ffff000010156008);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_CLK_FMEM_SUB_CFG_VDNR_DCM_TOP_AP_ffff000090800dba,&DAT_ffff00001412400c,
             _DAT_ffff00001412400c);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_CLK_FMEM_SUB_VDNR_DCM_TOP_APINFR_ffff0000907f66bd,&DAT_ffff000014124014,
             _DAT_ffff000014124014);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_CLK_FMEM_SUB_VDNR_DCM_TOP_APINFR_ffff0000907f4b81,&DAT_ffff000014124018,
             _DAT_ffff000014124018);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_CLK_FMEM_SUB_VDNR_DCM_TOP_APINFR_ffff0000907d2feb,&DAT_ffff00001412401c,
             _DAT_ffff00001412401c);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_CLK_FMEM_SUB_VDNR_DCM_TOP_APINFR_ffff0000907e4d0a,&DAT_ffff000014124020,
             _DAT_ffff000014124020);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_CLK_FMEM_SUB_VDNR_DCM_TOP_APINFR_ffff0000907f670a,&DAT_ffff000014124024,
             _DAT_ffff000014124024);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_CLK_FMEM_SUB_VDNR_DCM_TOP_APINFR_ffff0000907fd7ac,&DAT_ffff000014124028,
             _DAT_ffff000014124028);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_CLK_IO_NOC_VDNR_DCM_TOP_APINFRA__ffff0000907f4bce,&DAT_ffff000014012004,
             _DAT_ffff000014012004);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_ACP_SLAV_ffff000090800e0b,&DAT_ffff00000c1b02dc,
             _DAT_ffff00000c1b02dc);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_ADB_FIFO_ffff0000907ec82a,&DAT_ffff00000c1b0278,
             _DAT_ffff00000c1b0278);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907db031,&DAT_ffff00000c1b02ac,
             _DAT_ffff00000c1b02ac);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907fbb14,&DAT_ffff00000c1b02c4,
             _DAT_ffff00000c1b02c4);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907f8530,&DAT_ffff00000c1b02bc,
             _DAT_ffff00000c1b02bc);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907db061,&DAT_ffff00000c1b02a4,
             _DAT_ffff00000c1b02a4);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907fd7f9,&DAT_ffff00000c1b02b0,
             _DAT_ffff00000c1b02b0);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907de3fb,&DAT_ffff00000c1b02a0,
             _DAT_ffff00000c1b02a0);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907f9ebd,&DAT_ffff00000c1b02a8,
             _DAT_ffff00000c1b02a8);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_P2P_ffff000090802827,&DAT_ffff00000c1b02b4,
             _DAT_ffff00000c1b02b4);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_CI700_DC_ffff0000907f6757,&DAT_ffff00000c1b0298,
             _DAT_ffff00000c1b0298);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_EBG_CKE__ffff0000907d15a9,&DAT_ffff00000c1b0404,
             _DAT_ffff00000c1b0404);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_GIC_SPI__ffff0000907d3038,&DAT_ffff00000c1b02e0,
             _DAT_ffff00000c1b02e0);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_L3GIC_AR_ffff0000907e7c28,&DAT_ffff00000c1b0294,
             _DAT_ffff00000c1b0294);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_L3_SHARE_ffff0000907db091,&DAT_ffff00000c1b0078,
             _DAT_ffff00000c1b0078);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_MP0_DCM__ffff0000907ec852,&DAT_ffff00000c1b027c,
             _DAT_ffff00000c1b027c);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_MP_ADB_D_ffff0000907e64cd,&DAT_ffff00000c1b0270,
             _DAT_ffff00000c1b0270);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_MP_CENTR_ffff0000907e7c55,&DAT_ffff00000c1b02b8,
             _DAT_ffff00000c1b02b8);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_QDCM_CON_ffff0000907fd829,&DAT_ffff00000c1b0280,
             _DAT_ffff00000c1b0280);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_QDCM_CON_ffff0000907e96d0,&DAT_ffff00000c1b0284,
             _DAT_ffff00000c1b0284);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_QDCM_CON_ffff0000907fbb4a,&DAT_ffff00000c1b0288,
             _DAT_ffff00000c1b0288);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_MCUSYS_PAR_WRAP_NON_SEC_QDCM_CON_ffff0000907d490d,&DAT_ffff00000c1b028c,
             _DAT_ffff00000c1b028c);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_VDNR_DCM_TOP_APINFRA_MEM_INTF_PA_ffff0000907f677e,&DAT_ffff000014032000,
             _DAT_ffff000014032000);
  iVar1 = FUN_ffff0000907849e0();
  if (iVar1 == 0) {
    FUN_ffff00009077de54
              (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
               s_A0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907dfc7b,&DAT_ffff00001661002c,
               _DAT_ffff00001661002c);
    FUN_ffff00009077de54
              (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
               s_A0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907dcaa0,&DAT_ffff000016610030,
               _DAT_ffff000016610030);
    FUN_ffff00009077de54
              (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
               s_A0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907ff37c,&DAT_ffff000016610034,
               _DAT_ffff000016610034);
    puVar7 = &DAT_ffff000016610038;
    pcVar4 = s_A0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907d649b;
    uVar9 = _DAT_ffff000016610038;
  }
  else {
    FUN_ffff00009077de54
              (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
               s_B0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907d3064,&DAT_ffff000016610020,
               _DAT_ffff000016610020);
    FUN_ffff00009077de54
              (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
               s_B0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907e7c8e,&DAT_ffff000016610024,
               _DAT_ffff000016610024);
    FUN_ffff00009077de54
              (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
               s_B0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907ff3af,&DAT_ffff000016610028,
               _DAT_ffff000016610028);
    pcVar4 = s_B0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907e64f5;
    puVar7 = &DAT_ffff00001661002c;
    uVar9 = _DAT_ffff00001661002c;
  }
  FUN_ffff00009077de54(s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,pcVar4,puVar7,uVar9);
  FUN_ffff00009077de54
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7cd3,
             s_VDNR_DCM_TOP_VLP_PAR_BUS_TOP_u_V_ffff0000907f9eed,&DAT_ffff00001c03005c,
             _DAT_ffff00001c03005c);
  DAT_ffff000090877668 = 1;
  return 0;
}


