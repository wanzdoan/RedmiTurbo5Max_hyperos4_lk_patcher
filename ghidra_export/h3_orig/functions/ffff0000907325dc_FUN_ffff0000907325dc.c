// ffff0000907325dc  size=6932  FUN_ffff0000907325dc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907325dc(code *param_1)

{
  undefined4 *puVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  int iVar7;
  int iVar8;
  int iVar9;
  int iVar10;
  int iVar11;
  int iVar12;
  int iVar13;
  int iVar14;
  int iVar15;
  int iVar16;
  int iVar17;
  int iVar18;
  int iVar19;
  int iVar20;
  int iVar21;
  int iVar22;
  int iVar23;
  uint uVar24;
  int *piVar25;
  int iVar26;
  int iVar27;
  long lVar28;
  ulong uVar29;
  long lVar30;
  undefined8 uVar31;
  char *pcVar32;
  int *piVar33;
  ulong uVar34;
  ulong uVar35;
  uint *puVar36;
  char *pcVar37;
  undefined4 uVar38;
  undefined4 uVar39;
  ulong local_78;
  int *local_70;
  ulong local_68;
  
  local_70 = (int *)0x0;
  local_68 = 0;
  local_78 = 0;
  lVar28 = FUN_ffff00009077d24c(0x4800);
  if (lVar28 == 0) {
    FUN_ffff00009077de54
              (s__s__malloc_buffer_fail_ffff0000907d10aa,s__MFGSYS__EXPDB__ffff0000907de04f);
    return;
  }
  iVar26 = FUN_ffff000090783450(s_me_GPUEB_SHARED_ffff0000907e487b,&local_68,&local_78);
  uVar34 = local_68;
  uVar35 = local_78;
  if (iVar26 < 0) {
    thunk_FUN_ffff0000907c2ad8(lVar28);
    FUN_ffff00009077de54
              (s__s__get_reserved_shared_dram_fai_ffff0000907f9a64,
               s__MFGSYS__EXPDB__ffff0000907de04f);
    return;
  }
  if (local_68 != 0) {
    uVar29 = FUN_ffff00009071af94();
    lVar30 = FUN_ffff00009071aec4();
    if ((((uVar29 <= uVar34) && (uVar34 < lVar30 + uVar29)) && (!CARRY8(uVar35,uVar34))) &&
       (uVar35 + uVar34 < lVar30 + uVar29)) {
      iVar26 = FUN_ffff00009077b79c
                         (DAT_ffff000090839908,s_vm_GPU_shared_reserved_ffff0000907dc683,local_78,
                          &local_70,0xc,local_68,0,0);
      if (iVar26 != 0) {
        FUN_ffff00009077de54
                  (s__s__vmm_failed_for_gpu_shared_dr_ffff0000907d2b8c,
                   s__MFGSYS__EXPDB__ffff0000907de04f,iVar26);
      }
      piVar25 = local_70;
      if (*local_70 == -0x45452526) {
        iVar26 = FUN_ffff00009077f434(lVar28,0x4800,s__Segment_Info__ffff0000907e78ee);
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Segment_ID____d_ffff0000907f124c,piVar25[0x45]);
        lVar30 = (long)iVar26 + (long)iVar27;
        iVar27 = (int)lVar30;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + lVar30,(long)(0x4800 - iVar27),
                            s_Shader_Present__0x_08X_ffff0000907e1210,piVar25[0x36]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__GPU__Working_OPP_Number___3d_Si_ffff0000907d7952,piVar25[3],
                            piVar25[5]);
        iVar27 = iVar27 + iVar26;
        if (piVar25[0x44] != 0) {
          iVar26 = FUN_ffff00009077f434
                             (lVar28 + iVar27,(long)(0x4800 - iVar27),
                              s__STACK__Working_OPP_Number___3d_S_ffff0000907e9205,piVar25[4],
                              piVar25[6]);
          iVar27 = iVar26 + iVar27;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),s__Power_State__ffff0000908024ba
                           );
        lVar30 = (long)iVar27 + (long)iVar26;
        iVar26 = (int)lVar30;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (piVar25[0x33] != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + lVar30,(long)(0x4800 - iVar26),
                            s_Power_Control_Option____s_ffff0000907ec38a,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (piVar25[0x34] != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Active_Sleep_Control_Option___s_ffff0000907dc69d,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = s_POWER_ON_ffff0000907f802e;
        if (piVar25[7] < 1) {
          pcVar37 = s_POWER_OFF_ffff0000907d2bb9;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Current_Power_State____s_ffff0000907e923d,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = s_ACTIVE_ffff0000907f2ac9;
        if (piVar25[0xb] < 1) {
          pcVar37 = s_SLEEP_ffff0000907e925e;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Current_Active_Sleep_State____s_ffff0000907de05f,pcVar37);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Current_DVFS_State___0x_04x_ffff0000907e9264,piVar25[0x35]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Last_Power_Timestamp____lld_ffff0000907d44d7,
                            CONCAT44(piVar25[0x46],piVar25[0x47]));
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),s__Current_OPP__ffff0000907d2bc3
                           );
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__GPU__OPP_Index___2d_ffff0000907fb608,piVar25[1]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s__GPU__Freq___6d__6d__KHz__Volt____ffff0000907eab5a,piVar25[0x11],
                            piVar25[0x13],piVar25[0x17],piVar25[0x19]);
        iVar26 = iVar26 + iVar27;
        if (piVar25[0x44] != 0) {
          iVar27 = FUN_ffff00009077f434
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s__STACK__OPP_Index___2d_ffff0000907f2ad0,piVar25[2]);
          iVar27 = iVar27 + iVar26;
          iVar26 = FUN_ffff00009077f434
                             (lVar28 + iVar27,(long)(0x4800 - iVar27),
                              s__STACK__Freq___6d__6d__KHz__Volt_ffff0000907f1260,piVar25[0x12],
                              piVar25[0x14],piVar25[0x18],piVar25[0x1a]);
          iVar26 = iVar26 + iVar27;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__Current_Limit__ffff0000907f4702);
        lVar30 = (long)iVar26 + (long)iVar27;
        iVar27 = (int)lVar30;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + lVar30,(long)(0x4800 - iVar27),
                            s_Current_Ceiling___2d_Limiter___2_ffff0000907d60c2,piVar25[0x2d],
                            piVar25[0x2f],piVar25[0x31]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Current_Floor____2d_Limiter___2d_ffff0000907f6289,piVar25[0x2e],
                            piVar25[0x30],piVar25[0x32]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s__Power_Consumption__ffff0000907d10c2);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s__GPU__Current___4d__mW__Max___4d_ffff0000907fef42,piVar25[0x1f],
                            piVar25[0x21],piVar25[0x23]);
        iVar26 = iVar26 + iVar27;
        if (piVar25[0x44] != 0) {
          iVar27 = FUN_ffff00009077f434
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s__STACK__Current___4d__mW__Max____ffff0000907dc6be,piVar25[0x20],
                              piVar25[0x22],piVar25[0x24]);
          iVar26 = iVar27 + iVar26;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__Temperature_Info__ffff0000907e1228);
        lVar30 = (long)iVar26 + (long)iVar27;
        iVar27 = (int)lVar30;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + lVar30,(long)(0x4800 - iVar27),
                            s_GPU_Temperature____d___C__ffff0000907fb620,piVar25[0xc]);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (piVar25[0x4c] != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Temper_Compensate_Mode____s_ffff0000907de080,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (piVar25[0x4d] != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_HT_Temper_Compensate_Mode____s_ffff0000907d10d8,pcVar37);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_NORM_CON_GPU_Temper_Comp____d__K_ffff0000907de0a0,piVar25[0xd]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_HIGH_CON_GPU_Temper_Comp____d__K_ffff0000907fb645,piVar25[0xe]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_NORM_CON_STACK_Temper_Comp___d___ffff0000907eab9e,piVar25[0xf]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_HIGH_CON_STACK_Temper_Comp___d___ffff0000907fd1fb,piVar25[0x10]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__Leakage_Info__ffff0000907df8b8);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Leakage_RT_GPU___5d__STACK___5d__ffff0000907ec3ab,piVar25[0x25],
                            piVar25[0x26],piVar25[0x27]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Leakage_HT_GPU___5d__STACK___5d__ffff0000907de0d0,piVar25[0x28],
                            piVar25[0x29],piVar25[0x2a]);
        iVar2 = piVar25[0x8f5];
        iVar26 = iVar26 + iVar27;
        iVar3 = piVar25[0x8f6];
        iVar4 = piVar25[0x8fd];
        iVar5 = piVar25[0x8f3];
        iVar6 = piVar25[0x8fc];
        iVar7 = piVar25[0x8fe];
        iVar8 = piVar25[0x8f4];
        iVar9 = piVar25[0x8f7];
        iVar10 = piVar25[0x900];
        iVar11 = piVar25[0x8ff];
        iVar12 = piVar25[0x8f9];
        iVar13 = piVar25[0x901];
        iVar14 = piVar25[0x8fa];
        iVar15 = piVar25[0x902];
        iVar16 = piVar25[0x903];
        iVar17 = piVar25[0x904];
        iVar18 = piVar25[0x905];
        iVar19 = piVar25[0x906];
        iVar20 = piVar25[0x907];
        iVar21 = piVar25[0x90b];
        iVar22 = piVar25[0x90d];
        iVar23 = piVar25[0x90f];
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),s__PTP3_Info__ffff0000907de101);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (iVar9 != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_PTP3_Mode____s_ffff0000907ef8f1,pcVar37);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_PTP3_Debug_Mode___0x_08x_ffff0000907dc6f5,iVar9);
        iVar27 = iVar27 + iVar26;
        pcVar37 = s_SW_LOOP_ffff0000907d60f3;
        if (iVar5 != 1) {
          pcVar37 = s_LEGACY_ffff0000907e123d;
        }
        pcVar32 = s_HW_LOOP_ffff0000907d44fa;
        if (iVar5 != 2) {
          pcVar32 = pcVar37;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_DVFS_Mode____s_ffff0000907f4714,pcVar32);
        iVar27 = iVar27 + iVar26;
        pcVar37 = s_GPM3_0_ffff0000907d10f8;
        if (iVar8 != 1) {
          pcVar37 = &DAT_ffff0000907d44d3;
        }
        pcVar32 = s_GPM3_5_ffff0000907ef909;
        if (iVar8 != 2) {
          pcVar32 = pcVar37;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Imax_Protection____s_ffff0000907eabce,pcVar32);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (iVar12 != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_HBVC_Freq_Ctrl____s_ffff0000907f62ba,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (iVar14 != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_HBVC_Volt_Ctrl____s_ffff0000907fb675,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = s_AutoDMA_ffff0000907e2c46;
        if (iVar2 != 2) {
          pcVar37 = &DAT_ffff00009080094d;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_BRCAST_Refill_Mode___s_ffff0000907de10f,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907ec3dc;
        if (iVar3 != 1) {
          pcVar37 = &DAT_ffff00009080094d;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_DELSEL_Mode____s_ffff0000907f9a87,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (iVar6 != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_PreOC_Mode____s_ffff0000907fef79,pcVar37);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_PreOC_Count___d__Time___d__Facto_ffff0000907d4502,piVar25[0x918],
                            piVar25[0x919],piVar25[0x91a]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_HBVC_GPU_Vupper____5d__Vlower____ffff0000907cf716,iVar4,iVar7);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_HBVC_STACK_Vupper____5d__Vlower__ffff0000907f12a4,iVar11,iVar10);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (iVar13 != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Brisket_FLL____s_ffff0000907d60fb,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = s_UNSUPPORTED_ffff0000907daac9;
        if (iVar15 != -1) {
          pcVar37 = &DAT_ffff0000907d44d3;
        }
        pcVar32 = &DAT_ffff0000907dc69a;
        if (iVar15 != 1) {
          pcVar32 = pcVar37;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Brisket_ATMC____s_ffff0000907df8c9,pcVar32);
        iVar26 = iVar26 + iVar27;
        pcVar37 = s_UNSUPPORTED_ffff0000907daac9;
        if (iVar16 != -1) {
          pcVar37 = &DAT_ffff0000907d44d3;
        }
        pcVar32 = &DAT_ffff0000907dc69a;
        if (iVar16 != 1) {
          pcVar32 = pcVar37;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Brisket_Vmeter____s_ffff000090800950,pcVar32);
        iVar26 = iVar26 + iVar27;
        pcVar37 = s_UNSUPPORTED_ffff0000907daac9;
        if (iVar17 != -1) {
          pcVar37 = &DAT_ffff0000907d44d3;
        }
        pcVar32 = &DAT_ffff0000907dc69a;
        if (iVar17 != 1) {
          pcVar32 = pcVar37;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Brisket_Tmeter____s_ffff0000907e60c1,pcVar32);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (iVar18 != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Brisket_CPmeter____s_ffff0000907e1244,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (iVar19 != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Brisket_CTT____s_ffff0000907f12ca,pcVar37);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_InFreq____4d__4d__MHz__ffff0000907e60d9,piVar25[0x910],piVar25[0x912])
        ;
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_OutFreq___4d__4d__MHz__ffff0000907d798a,piVar25[0x911],piVar25[0x913])
        ;
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),s_CC____4d__4d_ffff0000907e48b4,
                            piVar25[0x914],piVar25[0x916]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),s_FC____4d__4d_ffff0000907ec3df,
                            piVar25[0x915],piVar25[0x917]);
        iVar26 = iVar26 + iVar27;
        pcVar37 = s_Normal_ffff0000907f921e;
        if (iVar20 != 0) {
          pcVar37 = &DAT_ffff0000907d4529;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),s_Mode____s_ffff0000907d79a2,
                            pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (iVar21 != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_SES_TOP_Mode____s_ffff0000907d91f5,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (iVar22 != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_SES_STACK_Mode____s_ffff000090800968,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (iVar23 != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_SES_Scheduler_Mode___s_ffff0000907dc711,pcVar37);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__Feature_Status__ffff0000907e48c6);
        iVar26 = iVar26 + iVar27;
        uVar24 = piVar25[0x49] - 4;
        if (uVar24 < 7) {
          pcVar37 = &DAT_ffff000090804888 + *(int *)(&DAT_ffff000090804888 + (long)(int)uVar24 * 4);
        }
        else {
          pcVar37 = &DAT_ffff0000907d611b;
          if (piVar25[0x49] != 0xb) {
            pcVar37 = s_Disable_ffff0000907daad5;
          }
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Stress_Test____s_ffff0000907ec3f1,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = s_Advanced_ffff0000907e125c;
        if (piVar25[0x4a] != 1) {
          pcVar37 = s_Normal_ffff0000907f921e;
        }
        pcVar32 = s_Privilege_ffff0000908024d4;
        if (piVar25[0x4a] != 2) {
          pcVar32 = pcVar37;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Test_Mode____s_ffff0000907df8e1,pcVar32);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (piVar25[0x37] != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Aging_Sensor___s_ffff0000907f9a9f,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (piVar25[0x38] != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Aging_Load____s_ffff0000907eabe6,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (piVar25[0x39] != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Aging_Margin___s_ffff0000907e7905,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (piVar25[0x3a] != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_AVS_Status____s_ffff0000907f12f6,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (piVar25[0x3b] != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_AVS_Margin____s_ffff0000907d2bd3,pcVar37);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_SB_Version___0x_04x_ffff0000907ee084,piVar25[0x3d]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_PTP_Version___0x_04x_ffff0000907e60f1,piVar25[0x3e]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_DBG_Version___0x_08X_ffff0000907f9ab1,piVar25[0x3f]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_KDBG_Version__0x_08X_ffff0000907f1308,piVar25[0x40]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),s_C_Type____d_ffff0000907fd22b,
                            piVar25[0x3c]);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (piVar25[0x41] != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),s_GPM_1_0____s_ffff0000907ef91c,
                            pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d44d3;
        if (piVar25[0x42] != 0) {
          pcVar37 = &DAT_ffff0000907dc69a;
        }
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),s_GPM_3_0____s_ffff0000907d79af,
                            pcVar37);
        iVar26 = iVar26 + iVar27;
        iVar27 = piVar25[0x43];
        if (iVar27 == 2) {
          pcVar37 = &DAT_ffff0000907e9289;
        }
        else if (iVar27 == 3) {
          pcVar37 = &DAT_ffff0000907e48d9;
        }
        else {
          pcVar37 = &DAT_ffff0000907dc730;
          if (iVar27 != 1) {
            pcVar37 = &DAT_ffff0000907d44d3;
          }
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),s_DFD____s_ffff0000907e7917,
                            pcVar37);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),s__Limit_Table__ffff0000907f8037
                           );
        iVar27 = iVar27 + iVar26;
        pcVar37 = s__c_enable__ffff0000907e1265;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s__4s__15s__11s__10s__8s__11s__11s_ffff0000907ef92e,
                            &DAT_ffff0000907e6107,s__name__ffff0000907df8f3,
                            s__priority__ffff0000907d452e,s__ceiling__ffff0000907e2c4e,
                            s__floor__ffff0000907dc734,s__c_enable__ffff0000907e1265,
                            s__f_enable__ffff0000907f62d2);
        uVar35 = (ulong)(uint)(iVar27 + iVar26);
        lVar30 = 0xe;
        puVar36 = (uint *)(piVar25 + 0x75f);
        do {
          iVar26 = (int)uVar35;
          pcVar37 = (char *)CONCAT44((int)((ulong)pcVar37 >> 0x20),puVar36[-2]);
          uVar34 = (ulong)*puVar36;
          iVar27 = FUN_ffff00009077f434
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s__4d__15s__11d__10d__8d__11d__11d_ffff0000907eabf8,puVar36[-10],
                              puVar36 + -9,puVar36[-4],puVar36[-3],puVar36[-1],pcVar37,uVar34);
          lVar30 = lVar30 + -1;
          uVar35 = (long)iVar26 + (long)iVar27;
          puVar36 = puVar36 + 0xb;
        } while (lVar30 != 0);
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + uVar35,(long)(0x4800 - (int)uVar35),
                            s__Bus_Tracker_Slave_Error__ffff0000907d4539);
        uVar29 = 0;
        uVar35 = (ulong)(uint)(iVar26 + (int)uVar35);
        piVar33 = piVar25 + 0x922;
        do {
          uVar24 = piVar33[-6];
          iVar26 = (int)uVar35;
          if (uVar24 < 5) {
            pcVar32 = (&PTR_s_VCORE_AR_ffff000090819288)[(int)uVar24];
          }
          else {
            pcVar32 = (char *)0xffff0000907ef950;
            if (uVar24 != 5) {
              pcVar32 = s_UNKNOWN_ffff0000907d6120;
            }
          }
          pcVar37 = (char *)CONCAT44((int)((ulong)pcVar37 >> 0x20),*piVar33);
          iVar27 = FUN_ffff00009077f434
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___02d___s___llu__Log_0x_08X__ID__ffff0000908024de,
                              uVar29 & 0xffffffff,pcVar32,*(undefined8 *)(piVar33 + -4),piVar33[-2],
                              piVar33[-1],pcVar37,uVar34);
          uVar29 = uVar29 + 1;
          uVar35 = (long)iVar26 + (long)iVar27;
          piVar33 = piVar33 + 8;
        } while (uVar29 != 10);
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + uVar35,(long)(0x4800 - (int)uVar35),
                            s__Bus_Tracker_Slave_Timeout__ffff0000907d79c1);
        uVar34 = 0;
        uVar35 = (ulong)(uint)(iVar26 + (int)uVar35);
        piVar33 = piVar25 + 0x972;
        do {
          uVar24 = piVar33[-6];
          iVar26 = (int)uVar35;
          if (uVar24 < 5) {
            pcVar32 = (&PTR_s_VCORE_AR_ffff000090819288)[(int)uVar24];
          }
          else {
            pcVar32 = (char *)0xffff0000907ef950;
            if (uVar24 != 5) {
              pcVar32 = s_UNKNOWN_ffff0000907d6120;
            }
          }
          pcVar37 = (char *)CONCAT44((int)((ulong)pcVar37 >> 0x20),*piVar33);
          iVar27 = FUN_ffff00009077f434
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___02d___s___llu__Log_0x_08X__ID__ffff0000908024de,
                              uVar34 & 0xffffffff,pcVar32,*(undefined8 *)(piVar33 + -4),piVar33[-2],
                              piVar33[-1],pcVar37);
          uVar34 = uVar34 + 1;
          uVar35 = (long)iVar26 + (long)iVar27;
          piVar33 = piVar33 + 8;
        } while (uVar34 != 10);
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + uVar35,(long)(0x4800 - (int)uVar35),
                            s__GPU_Margin_Table__ffff0000907d2be5);
        iVar26 = iVar26 + (int)uVar35;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s________Vaging_________Vavs__ffff0000907d4555);
        lVar30 = -0xa0;
        uVar35 = (ulong)(uint)(iVar26 + iVar27);
        do {
          iVar26 = (int)uVar35;
          iVar27 = FUN_ffff00009077f434
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___02d____8d___02d____6d_ffff0000907de130,
                              *(undefined4 *)((long)piVar25 + lVar30 + 0x205c),
                              *(undefined4 *)((long)piVar25 + lVar30 + 0x2064),
                              *(undefined4 *)((long)piVar25 + lVar30 + 0x219c),
                              *(undefined4 *)((long)piVar25 + lVar30 + 0x21a4));
          lVar30 = lVar30 + 0x10;
          uVar35 = (long)iVar26 + (long)iVar27;
        } while (lVar30 != 0);
        if (piVar25[0x44] != 0) {
          iVar26 = FUN_ffff00009077f434
                             (lVar28 + uVar35,(long)(0x4800 - (int)uVar35),
                              s__STACK_Margin_Table__ffff0000907ef959);
          iVar26 = iVar26 + (int)uVar35;
          iVar27 = FUN_ffff00009077f434
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s________Vaging_________Vavs__ffff0000907d4555);
          lVar30 = -0xa0;
          uVar35 = (ulong)(uint)(iVar26 + iVar27);
          do {
            iVar27 = (int)uVar35;
            iVar26 = FUN_ffff00009077f434
                               (lVar28 + iVar27,(long)(0x4800 - iVar27),
                                s___02d____8d___02d____6d_ffff0000907de130,
                                *(undefined4 *)((long)piVar25 + lVar30 + 0x20fc),
                                *(undefined4 *)((long)piVar25 + lVar30 + 0x2104),
                                *(undefined4 *)((long)piVar25 + lVar30 + 0x223c),
                                *(undefined4 *)((long)piVar25 + lVar30 + 0x2244));
            lVar30 = lVar30 + 0x10;
            uVar35 = (ulong)(uint)(iVar26 + iVar27);
          } while (lVar30 != 0);
        }
        iVar26 = (int)uVar35;
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__GPU_3_0_Limit_Table__ffff0000907fb695);
        lVar30 = (long)iVar26 + (long)iVar27;
        iVar27 = (int)lVar30;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + lVar30,(long)(0x4800 - iVar27),
                            s__Temper___Ceiling___I_STACK___I__ffff0000907fd23d);
        lVar30 = 0;
        uVar35 = (ulong)(uint)(iVar27 + iVar26);
        do {
          puVar1 = (undefined4 *)((long)piVar25 + lVar30 + 0x223c);
          iVar26 = (int)uVar35;
          iVar27 = FUN_ffff00009077f434
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___6d___9d__9d__8d__9d_ffff0000907de149,*puVar1,puVar1[1],puVar1[2],
                              puVar1[3],puVar1[4]);
          lVar30 = lVar30 + 0x14;
          uVar35 = (long)iVar26 + (long)iVar27;
        } while (lVar30 != 400);
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + uVar35,(long)(0x4800 - (int)uVar35),
                            s__Working_GPU_OPP_Table__ffff0000907dc73c);
        iVar26 = iVar26 + (int)uVar35;
        if (0 < piVar25[3]) {
          uVar35 = 0;
          piVar33 = piVar25 + 0xca;
          do {
            pcVar37 = (char *)CONCAT44((int)((ulong)pcVar37 >> 0x20),piVar33[-1]);
            iVar27 = FUN_ffff00009077f434
                               (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                s___02d__freq___7d__volt___6d__vsr_ffff0000907e48dd,
                                uVar35 & 0xffffffff,piVar33[-5],piVar33[-4],piVar33[-3],piVar33[-2],
                                pcVar37,*piVar33);
            iVar26 = iVar27 + iVar26;
            uVar35 = uVar35 + 1;
            piVar33 = piVar33 + 6;
          } while ((long)uVar35 < (long)piVar25[3]);
        }
        uVar39 = (undefined4)((ulong)pcVar37 >> 0x20);
        if (piVar25[0x44] != 0) {
          iVar27 = FUN_ffff00009077f434
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s__Working_STACK_OPP_Table__ffff0000907f8050);
          uVar39 = (undefined4)((ulong)pcVar37 >> 0x20);
          iVar26 = iVar27 + iVar26;
          if (0 < piVar25[4]) {
            uVar35 = 0;
            piVar33 = piVar25 + 0x26e;
            do {
              pcVar37 = (char *)CONCAT44((int)((ulong)pcVar37 >> 0x20),piVar33[-1]);
              iVar27 = FUN_ffff00009077f434
                                 (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                  s___02d__freq___7d__volt___6d__vsr_ffff0000907e48dd,
                                  uVar35 & 0xffffffff,piVar33[-5],piVar33[-4],piVar33[-3],
                                  piVar33[-2],pcVar37,*piVar33);
              uVar39 = (undefined4)((ulong)pcVar37 >> 0x20);
              iVar26 = iVar27 + iVar26;
              uVar35 = uVar35 + 1;
              piVar33 = piVar33 + 6;
            } while ((long)uVar35 < (long)piVar25[4]);
          }
        }
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__Register_Latch__ffff000090800980);
        lVar30 = (long)iVar26 + (long)iVar27;
        iVar27 = (int)lVar30;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + lVar30,(long)(0x4800 - iVar27),
                            s___40s__0x_08X___40s__0x_08X_ffff0000907ef970,
                            s_MFG_RPC_MFG0_PWR_CON_ffff0000907d79df,_DAT_ffff00004b800504,
                            s_MFG_RPC_MFG1_PWR_CON_ffff0000907e928d,_DAT_ffff00004b800500);
        iVar26 = iVar26 + iVar27;
        uVar31 = CONCAT44(uVar39,_DAT_ffff00004b8009ac);
        iVar27 = FUN_ffff00009077f434
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s___40s__0x_08X___40s__0x_08X___40_ffff0000907d79f4,
                            s_MFG_RPC_GHPM_RO0_CON_ffff000090800993,_DAT_ffff00004b8009a4,
                            s_MFG_RPC_GHPM_RO1_CON_ffff0000907e92a2,_DAT_ffff00004b8009a8,
                            s_MFG_RPC_GHPM_RO2_CON_ffff0000907ee09a,uVar31);
        iVar27 = iVar27 + iVar26;
        uVar31 = CONCAT44((int)((ulong)uVar31 >> 0x20),_DAT_ffff00004b90004c);
        pcVar37 = s_MFG_VCORE_BUS_SYSTIMER_LATCH_L_ffff0000907f806c;
        uVar39 = _DAT_ffff00004b900048;
        iVar26 = FUN_ffff00009077f434
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s___40s__0x_08X___40s__0x_08X___40_ffff0000907f9ad0,
                            s_MFG_VCORE_BUS_DBG_CON_ffff0000907fb6ad,_DAT_ffff00004b900000,
                            s_MFG_VCORE_BUS_TIMEOUT_INFO_ffff0000907cf73c,_DAT_ffff00004b900028,
                            s_MFG_VCORE_BUS_SYSTIMER_LATCH_H_ffff0000907f62dd,uVar31,
                            s_MFG_VCORE_BUS_SYSTIMER_LATCH_L_ffff0000907f806c,_DAT_ffff00004b900048)
        ;
        lVar30 = 0;
        iVar26 = iVar26 + iVar27;
        do {
          if (*(int *)(lVar30 + -0xffffb46ffe00) != 0) {
            uVar31 = CONCAT44((int)((ulong)uVar31 >> 0x20),*(undefined4 *)(lVar30 + -0xffffb46ffc00)
                             );
            iVar27 = FUN_ffff00009077f434
                               (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                s___40s__0x_08X___40s__0x_08X___40_ffff0000907d79f4,
                                s_MFG_VCORE_BUS_AR_TRACKER_LOG_ffff0000907ec403,
                                *(int *)(lVar30 + -0xffffb46ffe00),
                                s_MFG_VCORE_BUS_AR_TRACKER_ID_ffff0000907ef98d,
                                *(undefined4 *)(lVar30 + -0xffffb46ffd00),
                                s_MFG_VCORE_BUS_AR_TRACKER_L_ffff0000907e1270,uVar31,pcVar37,uVar39)
            ;
            iVar26 = iVar27 + iVar26;
          }
          lVar30 = lVar30 + 4;
        } while (lVar30 != 0x40);
        lVar30 = 0;
        do {
          if (*(int *)(lVar30 + -0xffffb46ff800) != 0) {
            uVar31 = CONCAT44((int)((ulong)uVar31 >> 0x20),*(undefined4 *)(lVar30 + -0xffffb46ff600)
                             );
            iVar27 = FUN_ffff00009077f434
                               (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                s___40s__0x_08X___40s__0x_08X___40_ffff0000907d79f4,
                                s_MFG_VCORE_BUS_AW_TRACKER_LOG_ffff0000907fd26e,
                                *(undefined4 *)(lVar30 + -0xffffb46ff800),
                                s_MFG_VCORE_BUS_AW_TRACKER_ID_ffff0000907e128b,
                                *(undefined4 *)(lVar30 + -0xffffb46ff700),
                                s_MFG_VCORE_BUS_AW_TRACKER_L_ffff0000907daadd,uVar31,pcVar37,uVar39)
            ;
            iVar26 = iVar27 + iVar26;
          }
          uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
          lVar30 = lVar30 + 4;
        } while (lVar30 != 0x40);
        if (_DAT_ffff00004b90008c != 0) {
          uVar31 = CONCAT44(uVar38,_DAT_ffff00004b900080);
          iVar27 = FUN_ffff00009077f434
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___40s__0x_08X___40s__0x_08X___40_ffff0000907d79f4,
                              s_MFG_VCORE_BUS_AR_SLVERR_LOG_ffff0000907d2bfa,_DAT_ffff00004b90008c,
                              s_MFG_VCORE_BUS_AR_SLVERR_ID_ffff0000907cf757,_DAT_ffff00004b900088,
                              s_MFG_VCORE_BUS_AR_SLVERR_ADDR_L_ffff0000907d2c16,uVar31);
          uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
          iVar26 = iVar27 + iVar26;
        }
        if (_DAT_ffff00004b90009c != 0) {
          uVar31 = CONCAT44(uVar38,_DAT_ffff00004b900090);
          iVar27 = FUN_ffff00009077f434
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___40s__0x_08X___40s__0x_08X___40_ffff0000907d79f4,
                              s_MFG_VCORE_BUS_AW_SLVERR_LOG_ffff0000907d6128,_DAT_ffff00004b90009c,
                              s_MFG_VCORE_BUS_AW_SLVERR_ID_ffff0000907f808b,_DAT_ffff00004b900098,
                              s_MFG_VCORE_BUS_AW_SLVERR_ADDR_L_ffff0000907e610c,uVar31);
          uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
          iVar26 = iVar27 + iVar26;
        }
        if (2 < _DAT_ffff00004b800504 >> 0x1e) {
          uVar31 = CONCAT44(uVar38,_DAT_ffff00004b1a004c);
          pcVar37 = s_MFG_GPUEB_BUS_SYSTIMER_LATCH_L_ffff0000907d4572;
          uVar39 = _DAT_ffff00004b1a0048;
          iVar27 = FUN_ffff00009077f434
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___40s__0x_08X___40s__0x_08X___40_ffff0000907f9ad0,
                              s_MFG_GPUEB_BUS_DBG_CON_ffff0000907df8fa,_DAT_ffff00004b1a0000,
                              s_MFG_GPUEB_BUS_TIMEOUT_INFO_ffff0000907ec420,_DAT_ffff00004b1a0028,
                              s_MFG_GPUEB_BUS_SYSTIMER_LATCH_H_ffff0000907e612b,uVar31,
                              s_MFG_GPUEB_BUS_SYSTIMER_LATCH_L_ffff0000907d4572,
                              _DAT_ffff00004b1a0048);
          lVar30 = 0;
          iVar27 = iVar27 + iVar26;
          do {
            if (*(int *)(lVar30 + -0xffffb4e5fe00) != 0) {
              uVar31 = CONCAT44((int)((ulong)uVar31 >> 0x20),
                                *(undefined4 *)(lVar30 + -0xffffb4e5fc00));
              iVar26 = FUN_ffff00009077f434
                                 (lVar28 + iVar27,(long)(0x4800 - iVar27),
                                  s___40s__0x_08X___40s__0x_08X___40_ffff0000907d79f4,
                                  s_MFG_GPUEB_BUS_AR_TRACKER_LOG_ffff0000907e492c,
                                  *(int *)(lVar30 + -0xffffb4e5fe00),
                                  s_MFG_GPUEB_BUS_AR_TRACKER_ID_ffff0000907cf772,
                                  *(undefined4 *)(lVar30 + -0xffffb4e5fd00),
                                  s_MFG_GPUEB_BUS_AR_TRACKER_L_ffff0000907d9215,uVar31,pcVar37,
                                  uVar39);
              iVar27 = iVar26 + iVar27;
            }
            lVar30 = lVar30 + 4;
          } while (lVar30 != 0x80);
          lVar30 = 0;
          do {
            if (*(int *)(lVar30 + -0xffffb4e5f800) != 0) {
              uVar31 = CONCAT44((int)((ulong)uVar31 >> 0x20),
                                *(undefined4 *)(lVar30 + -0xffffb4e5f600));
              iVar26 = FUN_ffff00009077f434
                                 (lVar28 + iVar27,(long)(0x4800 - iVar27),
                                  s___40s__0x_08X___40s__0x_08X___40_ffff0000907d79f4,
                                  s_MFG_GPUEB_BUS_AW_TRACKER_LOG_ffff0000908009a8,
                                  *(undefined4 *)(lVar30 + -0xffffb4e5f800),
                                  s_MFG_GPUEB_BUS_AW_TRACKER_ID_ffff0000907e12a7,
                                  *(undefined4 *)(lVar30 + -0xffffb4e5f700),
                                  s_MFG_GPUEB_BUS_AW_TRACKER_L_ffff0000907f80a6,uVar31,pcVar37,
                                  uVar39);
              iVar27 = iVar26 + iVar27;
            }
            uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
            lVar30 = lVar30 + 4;
          } while (lVar30 != 0x80);
          if (_DAT_ffff00004b1a008c != 0) {
            uVar31 = CONCAT44(uVar38,_DAT_ffff00004b1a0080);
            iVar26 = FUN_ffff00009077f434
                               (lVar28 + iVar27,(long)(0x4800 - iVar27),
                                s___40s__0x_08X___40s__0x_08X___40_ffff0000907d79f4,
                                s_MFG_GPUEB_BUS_AR_SLVERR_LOG_ffff0000907d10ff,_DAT_ffff00004b1a008c
                                ,s_MFG_GPUEB_BUS_AR_SLVERR_ID_ffff0000907ec43b,_DAT_ffff00004b1a0088
                                ,s_MFG_GPUEB_BUS_AR_SLVERR_ADDR_L_ffff0000907f80c1,uVar31);
            uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
            iVar27 = iVar26 + iVar27;
          }
          if (_DAT_ffff00004b1a009c != 0) {
            uVar31 = CONCAT44(uVar38,_DAT_ffff00004b1a0090);
            iVar26 = FUN_ffff00009077f434
                               (lVar28 + iVar27,(long)(0x4800 - iVar27),
                                s___40s__0x_08X___40s__0x_08X___40_ffff0000907d79f4,
                                s_MFG_GPUEB_BUS_AW_SLVERR_LOG_ffff0000907f80e0,_DAT_ffff00004b1a009c
                                ,s_MFG_GPUEB_BUS_AW_SLVERR_ID_ffff0000907d6144,_DAT_ffff00004b1a0098
                                ,s_MFG_GPUEB_BUS_AW_SLVERR_ADDR_L_ffff0000907e4949,uVar31);
            uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
            iVar27 = iVar26 + iVar27;
          }
          iVar26 = FUN_ffff00009077f434
                             (lVar28 + iVar27,(long)(0x4800 - iVar27),
                              s___40s__0x_08X_ffff0000907d4591,
                              s_GPUFREQ_FOOTPRINT_GPR_ffff0000907e92b7,_DAT_ffff00004b09fd44);
          iVar26 = iVar26 + iVar27;
        }
        if (2 < _DAT_ffff00004b800500 >> 0x1e) {
          uVar31 = CONCAT44(uVar38,_DAT_ffff00004880004c);
          pcVar37 = s_MFG_VGPU_BUS_SYSTIMER_LATCH_L_ffff0000907f62fc;
          uVar39 = _DAT_ffff000048800048;
          iVar27 = FUN_ffff00009077f434
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___40s__0x_08X___40s__0x_08X___40_ffff0000907f9ad0,
                              s_MFG_VGPU_BUS_DBG_CON_ffff0000907eac1a,_DAT_ffff000048800000,
                              s_MFG_VGPU_BUS_TIMEOUT_INFO_ffff0000907f9b09,_DAT_ffff000048800028,
                              s_MFG_VGPU_BUS_SYSTIMER_LATCH_H_ffff0000907d615f,uVar31,
                              s_MFG_VGPU_BUS_SYSTIMER_LATCH_L_ffff0000907f62fc,_DAT_ffff000048800048
                             );
          lVar30 = 0;
          iVar26 = iVar27 + iVar26;
          do {
            if (*(int *)(lVar30 + -0xffffb77ffe00) != 0) {
              uVar31 = CONCAT44((int)((ulong)uVar31 >> 0x20),
                                *(undefined4 *)(lVar30 + -0xffffb77ffc00));
              iVar27 = FUN_ffff00009077f434
                                 (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                  s___40s__0x_08X___40s__0x_08X___40_ffff0000907d79f4,
                                  s_MFG_VGPU_BUS_AR_TRACKER_LOG_ffff0000907e614a,
                                  *(int *)(lVar30 + -0xffffb77ffe00),
                                  s_MFG_VGPU_BUS_AR_TRACKER_ID_ffff0000907f472c,
                                  *(undefined4 *)(lVar30 + -0xffffb77ffd00),
                                  s_MFG_VGPU_BUS_AR_TRACKER_L_ffff0000907e2c58,uVar31,pcVar37,uVar39
                                 );
              iVar26 = iVar27 + iVar26;
            }
            lVar30 = lVar30 + 4;
          } while (lVar30 != 0x80);
          lVar30 = 0;
          do {
            if (*(int *)(lVar30 + -0xffffb77ff800) != 0) {
              uVar31 = CONCAT44((int)((ulong)uVar31 >> 0x20),
                                *(undefined4 *)(lVar30 + -0xffffb77ff600));
              iVar27 = FUN_ffff00009077f434
                                 (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                  s___40s__0x_08X___40s__0x_08X___40_ffff0000907d79f4,
                                  s_MFG_VGPU_BUS_AW_TRACKER_LOG_ffff0000907f2ae8,
                                  *(undefined4 *)(lVar30 + -0xffffb77ff800),
                                  s_MFG_VGPU_BUS_AW_TRACKER_ID_ffff0000907e92cd,
                                  *(undefined4 *)(lVar30 + -0xffffb77ff700),
                                  s_MFG_VGPU_BUS_AW_TRACKER_L_ffff0000907e92e8,uVar31,pcVar37,uVar39
                                 );
              iVar26 = iVar27 + iVar26;
            }
            uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
            lVar30 = lVar30 + 4;
          } while (lVar30 != 0x80);
          if (_DAT_ffff00004880008c != 0) {
            uVar31 = CONCAT44(uVar38,_DAT_ffff000048800080);
            iVar27 = FUN_ffff00009077f434
                               (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                s___40s__0x_08X___40s__0x_08X___40_ffff0000907d79f4,
                                s_MFG_VGPU_BUS_AR_SLVERR_LOG_ffff0000907e12c3,_DAT_ffff00004880008c,
                                s_MFG_VGPU_BUS_AR_SLVERR_ID_ffff0000907e9302,_DAT_ffff000048800088,
                                s_MFG_VGPU_BUS_AR_SLVERR_ADDR_L_ffff0000907ee0af,uVar31);
            uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
            iVar26 = iVar27 + iVar26;
          }
          if (_DAT_ffff00004880009c != 0) {
            iVar27 = FUN_ffff00009077f434
                               (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                s___40s__0x_08X___40s__0x_08X___40_ffff0000907d79f4,
                                s_MFG_VGPU_BUS_AW_SLVERR_LOG_ffff0000907d9230,_DAT_ffff00004880009c,
                                s_MFG_VGPU_BUS_AW_SLVERR_ID_ffff0000907e6166,_DAT_ffff000048800098,
                                s_MFG_VGPU_BUS_AW_SLVERR_ADDR_L_ffff0000907d111b,
                                CONCAT44(uVar38,_DAT_ffff000048800090));
            iVar26 = iVar27 + iVar26;
          }
        }
      }
      else {
        FUN_ffff00009077de54
                  (s__s__invalid_state_of_GPUEB_SHARE_ffff0000907e488b,
                   s__MFGSYS__EXPDB__ffff0000907de04f);
        iVar26 = 0;
      }
      uVar31 = FUN_ffff00009077b06c(lVar28);
      uVar35 = (*param_1)(uVar31,(long)iVar26);
      if ((uVar35 & 1) == 0) {
        FUN_ffff00009077de54
                  (s__s_SYS_GPU_MFGSYS_LATCH_LOG_dump_ffff0000907df891,
                   s__MFGSYS__EXPDB__ffff0000907de04f);
      }
      goto LAB_ffff000090733174;
    }
  }
  FUN_ffff00009077de54
            (s__s__invalid_GPUEB_SHARED__addr__0_ffff000090802484,s__MFGSYS__EXPDB__ffff0000907de04f
             ,local_68,local_78);
LAB_ffff000090733174:
  thunk_FUN_ffff0000907c2ad8(lVar28);
  return;
}


