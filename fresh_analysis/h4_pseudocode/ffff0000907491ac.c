
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907491ac(long param_1)

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
  
  if (DAT_ffff000090877700 == '\x01') {
    FUN_ffff00009077e11c
              (s__DCM__ERROR___s__failed_due_to_i_ffff0000907d15a5,s_mt_dcm_init_ffff0000907ec9a9);
    return 0;
  }
  local_38 = 0;
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s_lk_driver_fdt_is_NULL__ffff0000907fd93c);
  }
  iVar1 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_mt6991_dcm_ffff0000907d651d);
  if (iVar1 < 0) {
    pcVar4 = s__DCM__WARN___DCM__mediatek_dcm_n_ffff0000907dcb4a;
LAB_ffff00009074924c:
    FUN_ffff00009077e11c(pcVar4);
    uVar10 = 0;
  }
  else {
    puVar2 = (uint *)FUN_ffff00009076df9c(param_1,iVar1,s_mcu_disable_ffff0000907e31b6,&local_38);
    if (puVar2 == (uint *)0x0) {
      pcVar4 = s__DCM__WARN___DCM__get_property_i_ffff0000907ee551;
      goto LAB_ffff00009074924c;
    }
    uVar10 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
    uVar10 = uVar10 >> 0x10 | uVar10 << 0x10;
  }
  local_34 = 0;
  iVar1 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_mt6991_dcm_ffff0000907d651d);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c(s__DCM__WARN___DCM__mediatek_dcm_n_ffff0000907dcb4a);
joined_r0xffff0000907492b8:
    if (uVar10 == 0) goto LAB_ffff0000907492e8;
    uVar9 = 0x4002;
LAB_ffff0000907492d4:
    pcVar4 = s__DCM__WARN___DCM__MCU_DCM_is_dis_ffff0000907d2ff2;
  }
  else {
    piVar3 = (int *)FUN_ffff00009076df9c(param_1,iVar1,s_infra_disable_ffff0000907db136,&local_34);
    if (piVar3 == (int *)0x0) {
      FUN_ffff00009077e11c(s__DCM__WARN___DCM__get_property_i_ffff0000907ee551);
      goto joined_r0xffff0000907492b8;
    }
    if ((uVar10 != 0) && (*piVar3 != 0)) {
      FUN_ffff00009077e11c(s__DCM__WARN___DCM__Infra_DCM_is_d_ffff0000907fd90c);
      uVar9 = 0x4003;
      goto LAB_ffff0000907492d4;
    }
    pcVar4 = s__DCM__WARN___DCM__MCU_DCM_is_dis_ffff0000907d2ff2;
    if (uVar10 == 0) {
      pcVar4 = s__DCM__WARN___DCM__Infra_DCM_is_d_ffff0000907fd90c;
    }
    uVar9 = 0x4002;
    if (uVar10 == 0) {
      uVar9 = 1;
    }
    if (uVar10 == 0 && *piVar3 == 0) goto LAB_ffff0000907492e8;
  }
  FUN_ffff00009077e11c(pcVar4);
  FUN_ffff0000907498e0(uVar9);
LAB_ffff0000907492e8:
  uVar10 = DAT_ffff00009081a74c;
  FUN_ffff00009077e11c
            (s__DCM__WARN____s_type_init_dcm_ty_ffff000090800f43,s_dcm_set_default_ffff000090800f86,
             DAT_ffff00009081a74c,0);
  if (PTR_s_INFRA_DCM_ffff00009081a780 != (undefined *)0x0) {
    puVar5 = PTR_s_INFRA_DCM_ffff00009081a780;
    puVar11 = &DAT_ffff00009081a750;
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
        FUN_ffff00009077e11c
                  (s__DCM__INFO____s_0x_X__current_st_ffff0000907dfd08,puVar5,uVar6,uVar9,uVar8);
      }
      puVar5 = *(undefined **)(puVar11 + 0x1a);
      puVar11 = puVar11 + 0xe;
    } while (puVar5 != (undefined *)0x0);
  }
  FUN_ffff00009077e11c(s__DCM__INFO___________dcm_dump_re_ffff0000907e31ff);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_CLK_AXI_VDNR_DCM_TOP_APINFRA_IO__ffff0000907f8686,&DAT_ffff000010156008,
             _DAT_ffff000010156008);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_CLK_FMEM_SUB_CFG_VDNR_DCM_TOP_AP_ffff000090800f96,&DAT_ffff00001412400c,
             _DAT_ffff00001412400c);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_CLK_FMEM_SUB_VDNR_DCM_TOP_APINFR_ffff0000907f685e,&DAT_ffff000014124014,
             _DAT_ffff000014124014);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_CLK_FMEM_SUB_VDNR_DCM_TOP_APINFR_ffff0000907f4d05,&DAT_ffff000014124018,
             _DAT_ffff000014124018);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_CLK_FMEM_SUB_VDNR_DCM_TOP_APINFR_ffff0000907d3028,&DAT_ffff00001412401c,
             _DAT_ffff00001412401c);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_CLK_FMEM_SUB_VDNR_DCM_TOP_APINFR_ffff0000907e4e25,&DAT_ffff000014124020,
             _DAT_ffff000014124020);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_CLK_FMEM_SUB_VDNR_DCM_TOP_APINFR_ffff0000907f68ab,&DAT_ffff000014124024,
             _DAT_ffff000014124024);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_CLK_FMEM_SUB_VDNR_DCM_TOP_APINFR_ffff0000907fd954,&DAT_ffff000014124028,
             _DAT_ffff000014124028);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_CLK_IO_NOC_VDNR_DCM_TOP_APINFRA__ffff0000907f4d52,&DAT_ffff000014012004,
             _DAT_ffff000014012004);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_ACP_SLAV_ffff000090800fe7,&DAT_ffff00000c1b02dc,
             _DAT_ffff00000c1b02dc);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_ADB_FIFO_ffff0000907ec9c4,&DAT_ffff00000c1b0278,
             _DAT_ffff00000c1b0278);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907db153,&DAT_ffff00000c1b02ac,
             _DAT_ffff00000c1b02ac);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907fbcb0,&DAT_ffff00000c1b02c4,
             _DAT_ffff00000c1b02c4);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907f86cc,&DAT_ffff00000c1b02bc,
             _DAT_ffff00000c1b02bc);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907db183,&DAT_ffff00000c1b02a4,
             _DAT_ffff00000c1b02a4);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907fd9a1,&DAT_ffff00000c1b02b0,
             _DAT_ffff00000c1b02b0);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907de4b5,&DAT_ffff00000c1b02a0,
             _DAT_ffff00000c1b02a0);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_CAB_ffff0000907fa059,&DAT_ffff00000c1b02a8,
             _DAT_ffff00000c1b02a8);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_CBIP_P2P_ffff000090802a2b,&DAT_ffff00000c1b02b4,
             _DAT_ffff00000c1b02b4);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_CI700_DC_ffff0000907f68f8,&DAT_ffff00000c1b0298,
             _DAT_ffff00000c1b0298);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_EBG_CKE__ffff0000907d15e6,&DAT_ffff00000c1b0404,
             _DAT_ffff00000c1b0404);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_GIC_SPI__ffff0000907d3075,&DAT_ffff00000c1b02e0,
             _DAT_ffff00000c1b02e0);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_L3GIC_AR_ffff0000907e7d83,&DAT_ffff00000c1b0294,
             _DAT_ffff00000c1b0294);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_L3_SHARE_ffff0000907db1b3,&DAT_ffff00000c1b0078,
             _DAT_ffff00000c1b0078);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_MP0_DCM__ffff0000907ec9ec,&DAT_ffff00000c1b027c,
             _DAT_ffff00000c1b027c);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_MP_ADB_D_ffff0000907e65f8,&DAT_ffff00000c1b0270,
             _DAT_ffff00000c1b0270);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_MP_CENTR_ffff0000907e7db0,&DAT_ffff00000c1b02b8,
             _DAT_ffff00000c1b02b8);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_QDCM_CON_ffff0000907fd9d1,&DAT_ffff00000c1b0280,
             _DAT_ffff00000c1b0280);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_QDCM_CON_ffff0000907e982b,&DAT_ffff00000c1b0284,
             _DAT_ffff00000c1b0284);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_QDCM_CON_ffff0000907fbce6,&DAT_ffff00000c1b0288,
             _DAT_ffff00000c1b0288);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_MCUSYS_PAR_WRAP_NON_SEC_QDCM_CON_ffff0000907d494a,&DAT_ffff00000c1b028c,
             _DAT_ffff00000c1b028c);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_VDNR_DCM_TOP_APINFRA_MEM_INTF_PA_ffff0000907f691f,&DAT_ffff000014032000,
             _DAT_ffff000014032000);
  iVar1 = FUN_ffff000090784ca8();
  if (iVar1 == 0) {
    FUN_ffff00009077e11c
              (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
               s_A0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907dfd35,&DAT_ffff00001661002c,
               _DAT_ffff00001661002c);
    FUN_ffff00009077e11c
              (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
               s_A0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907dcb8d,&DAT_ffff000016610030,
               _DAT_ffff000016610030);
    FUN_ffff00009077e11c
              (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
               s_A0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907ff524,&DAT_ffff000016610034,
               _DAT_ffff000016610034);
    puVar7 = &DAT_ffff000016610038;
    pcVar4 = s_A0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907d6531;
    uVar9 = _DAT_ffff000016610038;
  }
  else {
    FUN_ffff00009077e11c
              (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
               s_B0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907d30a1,&DAT_ffff000016610020,
               _DAT_ffff000016610020);
    FUN_ffff00009077e11c
              (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
               s_B0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907e7de9,&DAT_ffff000016610024,
               _DAT_ffff000016610024);
    FUN_ffff00009077e11c
              (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
               s_B0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907ff557,&DAT_ffff000016610028,
               _DAT_ffff000016610028);
    pcVar4 = s_B0_VDNR_DCM_TOP_PERI_PAR_BUS_u_P_ffff0000907e6620;
    puVar7 = &DAT_ffff00001661002c;
    uVar9 = _DAT_ffff00001661002c;
  }
  FUN_ffff00009077e11c(s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,pcVar4,puVar7,uVar9);
  FUN_ffff00009077e11c
            (s__DCM__INFO____30s_0x_lx___0x_08x_ffff0000907d7d69,
             s_VDNR_DCM_TOP_VLP_PAR_BUS_TOP_u_V_ffff0000907fa089,&DAT_ffff00001c03005c,
             _DAT_ffff00001c03005c);
  DAT_ffff000090877700 = 1;
  return 0;
}


