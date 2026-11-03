// ffff000090732874  size=6932  FUN_ffff000090732874

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090732874(code *param_1)

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
  lVar28 = FUN_ffff00009077d514(0x4800);
  if (lVar28 == 0) {
    FUN_ffff00009077e11c
              (s__s__malloc_buffer_fail_ffff0000907d10e7,s__MFGSYS__EXPDB__ffff0000907de0f2);
    return;
  }
  iVar26 = FUN_ffff000090783718(s_me_GPUEB_SHARED_ffff0000907e497f,&local_68,&local_78);
  uVar34 = local_68;
  uVar35 = local_78;
  if (iVar26 < 0) {
    thunk_FUN_ffff0000907c314c(lVar28);
    FUN_ffff00009077e11c
              (s__s__get_reserved_shared_dram_fai_ffff0000907f9c00,
               s__MFGSYS__EXPDB__ffff0000907de0f2);
    return;
  }
  if (local_68 != 0) {
    uVar29 = FUN_ffff00009071b0d8();
    lVar30 = FUN_ffff00009071b008();
    if ((((uVar29 <= uVar34) && (uVar34 < lVar30 + uVar29)) && (!CARRY8(uVar35,uVar34))) &&
       (uVar35 + uVar34 < lVar30 + uVar29)) {
      iVar26 = FUN_ffff00009077ba64
                         (DAT_ffff000090839928,s_vm_GPU_shared_reserved_ffff0000907dc759,local_78,
                          &local_70,0xc,local_68,0,0);
      if (iVar26 != 0) {
        FUN_ffff00009077e11c
                  (s__s__vmm_failed_for_gpu_shared_dr_ffff0000907d2bc9,
                   s__MFGSYS__EXPDB__ffff0000907de0f2,iVar26);
      }
      piVar25 = local_70;
      if (*local_70 == -0x45452526) {
        iVar26 = FUN_ffff00009077f6fc(lVar28,0x4800,s__Segment_Info__ffff0000907e7a32);
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Segment_ID____d_ffff0000907f13bd,piVar25[0x45]);
        lVar30 = (long)iVar26 + (long)iVar27;
        iVar27 = (int)lVar30;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + lVar30,(long)(0x4800 - iVar27),
                            s_Shader_Present__0x_08X_ffff0000907e12e1,piVar25[0x36]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__GPU__Working_OPP_Number___3d_Si_ffff0000907d79e8,piVar25[3],
                            piVar25[5]);
        iVar27 = iVar27 + iVar26;
        if (piVar25[0x44] != 0) {
          iVar26 = FUN_ffff00009077f6fc
                             (lVar28 + iVar27,(long)(0x4800 - iVar27),
                              s__STACK__Working_OPP_Number___3d_S_ffff0000907e9349,piVar25[4],
                              piVar25[6]);
          iVar27 = iVar26 + iVar27;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),s__Power_State__ffff0000908026be
                           );
        lVar30 = (long)iVar27 + (long)iVar26;
        iVar26 = (int)lVar30;
        pcVar37 = &DAT_ffff0000907d4510;
        if (piVar25[0x33] != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + lVar30,(long)(0x4800 - iVar26),
                            s_Power_Control_Option____s_ffff0000907ec50d,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d4510;
        if (piVar25[0x34] != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Active_Sleep_Control_Option___s_ffff0000907dc773,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = s_POWER_ON_ffff0000907f81ca;
        if (piVar25[7] < 1) {
          pcVar37 = s_POWER_OFF_ffff0000907d2bf6;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Current_Power_State____s_ffff0000907e9381,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = s_ACTIVE_ffff0000907f2c55;
        if (piVar25[0xb] < 1) {
          pcVar37 = s_SLEEP_ffff0000907e93a2;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Current_Active_Sleep_State____s_ffff0000907de102,pcVar37);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Current_DVFS_State___0x_04x_ffff0000907e93a8,piVar25[0x35]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Last_Power_Timestamp____lld_ffff0000907d4514,
                            CONCAT44(piVar25[0x46],piVar25[0x47]));
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),s__Current_OPP__ffff0000907d2c00
                           );
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__GPU__OPP_Index___2d_ffff0000907fb7a4,piVar25[1]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s__GPU__Freq___6d__6d__KHz__Volt____ffff0000907eacdd,piVar25[0x11],
                            piVar25[0x13],piVar25[0x17],piVar25[0x19]);
        iVar26 = iVar26 + iVar27;
        if (piVar25[0x44] != 0) {
          iVar27 = FUN_ffff00009077f6fc
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s__STACK__OPP_Index___2d_ffff0000907f2c5c,piVar25[2]);
          iVar27 = iVar27 + iVar26;
          iVar26 = FUN_ffff00009077f6fc
                             (lVar28 + iVar27,(long)(0x4800 - iVar27),
                              s__STACK__Freq___6d__6d__KHz__Volt_ffff0000907f13d1,piVar25[0x12],
                              piVar25[0x14],piVar25[0x18],piVar25[0x1a]);
          iVar26 = iVar26 + iVar27;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__Current_Limit__ffff0000907f486f);
        lVar30 = (long)iVar26 + (long)iVar27;
        iVar27 = (int)lVar30;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + lVar30,(long)(0x4800 - iVar27),
                            s_Current_Ceiling___2d_Limiter___2_ffff0000907d6158,piVar25[0x2d],
                            piVar25[0x2f],piVar25[0x31]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Current_Floor____2d_Limiter___2d_ffff0000907f6413,piVar25[0x2e],
                            piVar25[0x30],piVar25[0x32]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s__Power_Consumption__ffff0000907d10ff);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s__GPU__Current___4d__mW__Max___4d_ffff0000907ff0ea,piVar25[0x1f],
                            piVar25[0x21],piVar25[0x23]);
        iVar26 = iVar26 + iVar27;
        if (piVar25[0x44] != 0) {
          iVar27 = FUN_ffff00009077f6fc
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s__STACK__Current___4d__mW__Max____ffff0000907dc794,piVar25[0x20],
                              piVar25[0x22],piVar25[0x24]);
          iVar26 = iVar27 + iVar26;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__Temperature_Info__ffff0000907e12f9);
        lVar30 = (long)iVar26 + (long)iVar27;
        iVar27 = (int)lVar30;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + lVar30,(long)(0x4800 - iVar27),
                            s_GPU_Temperature____d___C__ffff0000907fb7bc,piVar25[0xc]);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d4510;
        if (piVar25[0x4c] != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Temper_Compensate_Mode____s_ffff0000907de123,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d4510;
        if (piVar25[0x4d] != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_HT_Temper_Compensate_Mode____s_ffff0000907d1115,pcVar37);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_NORM_CON_GPU_Temper_Comp____d__K_ffff0000907de143,piVar25[0xd]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_HIGH_CON_GPU_Temper_Comp____d__K_ffff0000907fb7e1,piVar25[0xe]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_NORM_CON_STACK_Temper_Comp___d___ffff0000907ead21,piVar25[0xf]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_HIGH_CON_STACK_Temper_Comp___d___ffff0000907fd3a3,piVar25[0x10]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__Leakage_Info__ffff0000907df95b);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Leakage_RT_GPU___5d__STACK___5d__ffff0000907ec52e,piVar25[0x25],
                            piVar25[0x26],piVar25[0x27]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Leakage_HT_GPU___5d__STACK___5d__ffff0000907de173,piVar25[0x28],
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
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),s__PTP3_Info__ffff0000907de1a4);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d4510;
        if (iVar9 != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_PTP3_Mode____s_ffff0000907efa62,pcVar37);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_PTP3_Debug_Mode___0x_08x_ffff0000907dc7cb,iVar9);
        iVar27 = iVar27 + iVar26;
        pcVar37 = s_SW_LOOP_ffff0000907d6189;
        if (iVar5 != 1) {
          pcVar37 = s_LEGACY_ffff0000907e130e;
        }
        pcVar32 = s_HW_LOOP_ffff0000907d4537;
        if (iVar5 != 2) {
          pcVar32 = pcVar37;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_DVFS_Mode____s_ffff0000907f4881,pcVar32);
        iVar27 = iVar27 + iVar26;
        pcVar37 = s_GPM3_0_ffff0000907d1135;
        if (iVar8 != 1) {
          pcVar37 = &DAT_ffff0000907d4510;
        }
        pcVar32 = s_GPM3_5_ffff0000907efa7a;
        if (iVar8 != 2) {
          pcVar32 = pcVar37;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Imax_Protection____s_ffff0000907ead51,pcVar32);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d4510;
        if (iVar12 != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_HBVC_Freq_Ctrl____s_ffff0000907f6444,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d4510;
        if (iVar14 != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_HBVC_Volt_Ctrl____s_ffff0000907fb811,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = s_AutoDMA_ffff0000907e2d17;
        if (iVar2 != 2) {
          pcVar37 = &DAT_ffff000090800b29;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_BRCAST_Refill_Mode___s_ffff0000907de1b2,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907ec55f;
        if (iVar3 != 1) {
          pcVar37 = &DAT_ffff000090800b29;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_DELSEL_Mode____s_ffff0000907f9c23,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d4510;
        if (iVar6 != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_PreOC_Mode____s_ffff0000907ff121,pcVar37);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_PreOC_Count___d__Time___d__Facto_ffff0000907d453f,piVar25[0x918],
                            piVar25[0x919],piVar25[0x91a]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_HBVC_GPU_Vupper____5d__Vlower____ffff0000907cf735,iVar4,iVar7);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_HBVC_STACK_Vupper____5d__Vlower__ffff0000907f1415,iVar11,iVar10);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d4510;
        if (iVar13 != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Brisket_FLL____s_ffff0000907d6191,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = s_UNSUPPORTED_ffff0000907dabd4;
        if (iVar15 != -1) {
          pcVar37 = &DAT_ffff0000907d4510;
        }
        pcVar32 = &DAT_ffff0000907dc770;
        if (iVar15 != 1) {
          pcVar32 = pcVar37;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Brisket_ATMC____s_ffff0000907df96c,pcVar32);
        iVar26 = iVar26 + iVar27;
        pcVar37 = s_UNSUPPORTED_ffff0000907dabd4;
        if (iVar16 != -1) {
          pcVar37 = &DAT_ffff0000907d4510;
        }
        pcVar32 = &DAT_ffff0000907dc770;
        if (iVar16 != 1) {
          pcVar32 = pcVar37;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Brisket_Vmeter____s_ffff000090800b2c,pcVar32);
        iVar26 = iVar26 + iVar27;
        pcVar37 = s_UNSUPPORTED_ffff0000907dabd4;
        if (iVar17 != -1) {
          pcVar37 = &DAT_ffff0000907d4510;
        }
        pcVar32 = &DAT_ffff0000907dc770;
        if (iVar17 != 1) {
          pcVar32 = pcVar37;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Brisket_Tmeter____s_ffff0000907e61d5,pcVar32);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d4510;
        if (iVar18 != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Brisket_CPmeter____s_ffff0000907e1315,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d4510;
        if (iVar19 != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Brisket_CTT____s_ffff0000907f143b,pcVar37);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_InFreq____4d__4d__MHz__ffff0000907e61ed,piVar25[0x910],piVar25[0x912])
        ;
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_OutFreq___4d__4d__MHz__ffff0000907d7a20,piVar25[0x911],piVar25[0x913])
        ;
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),s_CC____4d__4d_ffff0000907e49b8,
                            piVar25[0x914],piVar25[0x916]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),s_FC____4d__4d_ffff0000907ec562,
                            piVar25[0x915],piVar25[0x917]);
        iVar26 = iVar26 + iVar27;
        pcVar37 = s_Normal_ffff0000907f93ba;
        if (iVar20 != 0) {
          pcVar37 = &DAT_ffff0000907d4566;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),s_Mode____s_ffff0000907d7a38,
                            pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d4510;
        if (iVar21 != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_SES_TOP_Mode____s_ffff0000907d928c,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d4510;
        if (iVar22 != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_SES_STACK_Mode____s_ffff000090800b44,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d4510;
        if (iVar23 != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_SES_Scheduler_Mode___s_ffff0000907dc7e7,pcVar37);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__Feature_Status__ffff0000907e49ca);
        iVar26 = iVar26 + iVar27;
        uVar24 = piVar25[0x49] - 4;
        if (uVar24 < 7) {
          pcVar37 = &DAT_ffff000090804ab0 + *(int *)(&DAT_ffff000090804ab0 + (long)(int)uVar24 * 4);
        }
        else {
          pcVar37 = &DAT_ffff0000907d61b1;
          if (piVar25[0x49] != 0xb) {
            pcVar37 = s_Disable_ffff0000907dabe0;
          }
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Stress_Test____s_ffff0000907ec574,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = s_Advanced_ffff0000907e132d;
        if (piVar25[0x4a] != 1) {
          pcVar37 = s_Normal_ffff0000907f93ba;
        }
        pcVar32 = s_Privilege_ffff0000908026d8;
        if (piVar25[0x4a] != 2) {
          pcVar32 = pcVar37;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Test_Mode____s_ffff0000907df984,pcVar32);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d4510;
        if (piVar25[0x37] != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Aging_Sensor___s_ffff0000907f9c3b,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d4510;
        if (piVar25[0x38] != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_Aging_Load____s_ffff0000907ead69,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d4510;
        if (piVar25[0x39] != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_Aging_Margin___s_ffff0000907e7a49,pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d4510;
        if (piVar25[0x3a] != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_AVS_Status____s_ffff0000907f1467,pcVar37);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d4510;
        if (piVar25[0x3b] != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_AVS_Margin____s_ffff0000907d2c10,pcVar37);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_SB_Version___0x_04x_ffff0000907ee1f5,piVar25[0x3d]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_PTP_Version___0x_04x_ffff0000907e6205,piVar25[0x3e]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s_DBG_Version___0x_08X_ffff0000907f9c4d,piVar25[0x3f]);
        iVar26 = iVar26 + iVar27;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s_KDBG_Version__0x_08X_ffff0000907f1479,piVar25[0x40]);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),s_C_Type____d_ffff0000907fd3d3,
                            piVar25[0x3c]);
        iVar26 = iVar26 + iVar27;
        pcVar37 = &DAT_ffff0000907d4510;
        if (piVar25[0x41] != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),s_GPM_1_0____s_ffff0000907efa8d,
                            pcVar37);
        iVar27 = iVar27 + iVar26;
        pcVar37 = &DAT_ffff0000907d4510;
        if (piVar25[0x42] != 0) {
          pcVar37 = &DAT_ffff0000907dc770;
        }
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),s_GPM_3_0____s_ffff0000907d7a45,
                            pcVar37);
        iVar26 = iVar26 + iVar27;
        iVar27 = piVar25[0x43];
        if (iVar27 == 2) {
          pcVar37 = &DAT_ffff0000907e93cd;
        }
        else if (iVar27 == 3) {
          pcVar37 = &DAT_ffff0000907e49dd;
        }
        else {
          pcVar37 = &DAT_ffff0000907dc806;
          if (iVar27 != 1) {
            pcVar37 = &DAT_ffff0000907d4510;
          }
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),s_DFD____s_ffff0000907e7a5b,
                            pcVar37);
        iVar27 = iVar27 + iVar26;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),s__Limit_Table__ffff0000907f81d3
                           );
        iVar27 = iVar27 + iVar26;
        pcVar37 = s__c_enable__ffff0000907e1336;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s__4s__15s__11s__10s__8s__11s__11s_ffff0000907efa9f,
                            &DAT_ffff0000907e621b,s__name__ffff0000907df996,
                            s__priority__ffff0000907d456b,s__ceiling__ffff0000907e2d1f,
                            s__floor__ffff0000907dc80a,s__c_enable__ffff0000907e1336,
                            s__f_enable__ffff0000907f645c);
        uVar35 = (ulong)(uint)(iVar27 + iVar26);
        lVar30 = 0xe;
        puVar36 = (uint *)(piVar25 + 0x75f);
        do {
          iVar26 = (int)uVar35;
          pcVar37 = (char *)CONCAT44((int)((ulong)pcVar37 >> 0x20),puVar36[-2]);
          uVar34 = (ulong)*puVar36;
          iVar27 = FUN_ffff00009077f6fc
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s__4d__15s__11d__10d__8d__11d__11d_ffff0000907ead7b,puVar36[-10],
                              puVar36 + -9,puVar36[-4],puVar36[-3],puVar36[-1],pcVar37,uVar34);
          lVar30 = lVar30 + -1;
          uVar35 = (long)iVar26 + (long)iVar27;
          puVar36 = puVar36 + 0xb;
        } while (lVar30 != 0);
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + uVar35,(long)(0x4800 - (int)uVar35),
                            s__Bus_Tracker_Slave_Error__ffff0000907d4576);
        uVar29 = 0;
        uVar35 = (ulong)(uint)(iVar26 + (int)uVar35);
        piVar33 = piVar25 + 0x922;
        do {
          uVar24 = piVar33[-6];
          iVar26 = (int)uVar35;
          if (uVar24 < 5) {
            pcVar32 = (&PTR_s_VCORE_AR_ffff0000908192a8)[(int)uVar24];
          }
          else {
            pcVar32 = (char *)0xffff0000907efac1;
            if (uVar24 != 5) {
              pcVar32 = s_UNKNOWN_ffff0000907d61b6;
            }
          }
          pcVar37 = (char *)CONCAT44((int)((ulong)pcVar37 >> 0x20),*piVar33);
          iVar27 = FUN_ffff00009077f6fc
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___02d___s___llu__Log_0x_08X__ID__ffff0000908026e2,
                              uVar29 & 0xffffffff,pcVar32,*(undefined8 *)(piVar33 + -4),piVar33[-2],
                              piVar33[-1],pcVar37,uVar34);
          uVar29 = uVar29 + 1;
          uVar35 = (long)iVar26 + (long)iVar27;
          piVar33 = piVar33 + 8;
        } while (uVar29 != 10);
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + uVar35,(long)(0x4800 - (int)uVar35),
                            s__Bus_Tracker_Slave_Timeout__ffff0000907d7a57);
        uVar34 = 0;
        uVar35 = (ulong)(uint)(iVar26 + (int)uVar35);
        piVar33 = piVar25 + 0x972;
        do {
          uVar24 = piVar33[-6];
          iVar26 = (int)uVar35;
          if (uVar24 < 5) {
            pcVar32 = (&PTR_s_VCORE_AR_ffff0000908192a8)[(int)uVar24];
          }
          else {
            pcVar32 = (char *)0xffff0000907efac1;
            if (uVar24 != 5) {
              pcVar32 = s_UNKNOWN_ffff0000907d61b6;
            }
          }
          pcVar37 = (char *)CONCAT44((int)((ulong)pcVar37 >> 0x20),*piVar33);
          iVar27 = FUN_ffff00009077f6fc
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___02d___s___llu__Log_0x_08X__ID__ffff0000908026e2,
                              uVar34 & 0xffffffff,pcVar32,*(undefined8 *)(piVar33 + -4),piVar33[-2],
                              piVar33[-1],pcVar37);
          uVar34 = uVar34 + 1;
          uVar35 = (long)iVar26 + (long)iVar27;
          piVar33 = piVar33 + 8;
        } while (uVar34 != 10);
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + uVar35,(long)(0x4800 - (int)uVar35),
                            s__GPU_Margin_Table__ffff0000907d2c22);
        iVar26 = iVar26 + (int)uVar35;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s________Vaging_________Vavs__ffff0000907d4592);
        lVar30 = -0xa0;
        uVar35 = (ulong)(uint)(iVar26 + iVar27);
        do {
          iVar26 = (int)uVar35;
          iVar27 = FUN_ffff00009077f6fc
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___02d____8d___02d____6d_ffff0000907de1d3,
                              *(undefined4 *)((long)piVar25 + lVar30 + 0x205c),
                              *(undefined4 *)((long)piVar25 + lVar30 + 0x2064),
                              *(undefined4 *)((long)piVar25 + lVar30 + 0x219c),
                              *(undefined4 *)((long)piVar25 + lVar30 + 0x21a4));
          lVar30 = lVar30 + 0x10;
          uVar35 = (long)iVar26 + (long)iVar27;
        } while (lVar30 != 0);
        if (piVar25[0x44] != 0) {
          iVar26 = FUN_ffff00009077f6fc
                             (lVar28 + uVar35,(long)(0x4800 - (int)uVar35),
                              s__STACK_Margin_Table__ffff0000907efaca);
          iVar26 = iVar26 + (int)uVar35;
          iVar27 = FUN_ffff00009077f6fc
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s________Vaging_________Vavs__ffff0000907d4592);
          lVar30 = -0xa0;
          uVar35 = (ulong)(uint)(iVar26 + iVar27);
          do {
            iVar27 = (int)uVar35;
            iVar26 = FUN_ffff00009077f6fc
                               (lVar28 + iVar27,(long)(0x4800 - iVar27),
                                s___02d____8d___02d____6d_ffff0000907de1d3,
                                *(undefined4 *)((long)piVar25 + lVar30 + 0x20fc),
                                *(undefined4 *)((long)piVar25 + lVar30 + 0x2104),
                                *(undefined4 *)((long)piVar25 + lVar30 + 0x223c),
                                *(undefined4 *)((long)piVar25 + lVar30 + 0x2244));
            lVar30 = lVar30 + 0x10;
            uVar35 = (ulong)(uint)(iVar26 + iVar27);
          } while (lVar30 != 0);
        }
        iVar26 = (int)uVar35;
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__GPU_3_0_Limit_Table__ffff0000907fb831);
        lVar30 = (long)iVar26 + (long)iVar27;
        iVar27 = (int)lVar30;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + lVar30,(long)(0x4800 - iVar27),
                            s__Temper___Ceiling___I_STACK___I__ffff0000907fd3e5);
        lVar30 = 0;
        uVar35 = (ulong)(uint)(iVar27 + iVar26);
        do {
          puVar1 = (undefined4 *)((long)piVar25 + lVar30 + 0x223c);
          iVar26 = (int)uVar35;
          iVar27 = FUN_ffff00009077f6fc
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___6d___9d__9d__8d__9d_ffff0000907de1ec,*puVar1,puVar1[1],puVar1[2],
                              puVar1[3],puVar1[4]);
          lVar30 = lVar30 + 0x14;
          uVar35 = (long)iVar26 + (long)iVar27;
        } while (lVar30 != 400);
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + uVar35,(long)(0x4800 - (int)uVar35),
                            s__Working_GPU_OPP_Table__ffff0000907dc812);
        iVar26 = iVar26 + (int)uVar35;
        if (0 < piVar25[3]) {
          uVar35 = 0;
          piVar33 = piVar25 + 0xca;
          do {
            pcVar37 = (char *)CONCAT44((int)((ulong)pcVar37 >> 0x20),piVar33[-1]);
            iVar27 = FUN_ffff00009077f6fc
                               (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                s___02d__freq___7d__volt___6d__vsr_ffff0000907e49e1,
                                uVar35 & 0xffffffff,piVar33[-5],piVar33[-4],piVar33[-3],piVar33[-2],
                                pcVar37,*piVar33);
            iVar26 = iVar27 + iVar26;
            uVar35 = uVar35 + 1;
            piVar33 = piVar33 + 6;
          } while ((long)uVar35 < (long)piVar25[3]);
        }
        uVar39 = (undefined4)((ulong)pcVar37 >> 0x20);
        if (piVar25[0x44] != 0) {
          iVar27 = FUN_ffff00009077f6fc
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s__Working_STACK_OPP_Table__ffff0000907f81ec);
          uVar39 = (undefined4)((ulong)pcVar37 >> 0x20);
          iVar26 = iVar27 + iVar26;
          if (0 < piVar25[4]) {
            uVar35 = 0;
            piVar33 = piVar25 + 0x26e;
            do {
              pcVar37 = (char *)CONCAT44((int)((ulong)pcVar37 >> 0x20),piVar33[-1]);
              iVar27 = FUN_ffff00009077f6fc
                                 (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                  s___02d__freq___7d__volt___6d__vsr_ffff0000907e49e1,
                                  uVar35 & 0xffffffff,piVar33[-5],piVar33[-4],piVar33[-3],
                                  piVar33[-2],pcVar37,*piVar33);
              uVar39 = (undefined4)((ulong)pcVar37 >> 0x20);
              iVar26 = iVar27 + iVar26;
              uVar35 = uVar35 + 1;
              piVar33 = piVar33 + 6;
            } while ((long)uVar35 < (long)piVar25[4]);
          }
        }
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s__Register_Latch__ffff000090800b5c);
        lVar30 = (long)iVar26 + (long)iVar27;
        iVar27 = (int)lVar30;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + lVar30,(long)(0x4800 - iVar27),
                            s___40s__0x_08X___40s__0x_08X_ffff0000907efae1,
                            s_MFG_RPC_MFG0_PWR_CON_ffff0000907d7a75,_DAT_ffff00004b800504,
                            s_MFG_RPC_MFG1_PWR_CON_ffff0000907e93d1,_DAT_ffff00004b800500);
        iVar26 = iVar26 + iVar27;
        uVar31 = CONCAT44(uVar39,_DAT_ffff00004b8009ac);
        iVar27 = FUN_ffff00009077f6fc
                           (lVar28 + iVar26,(long)(0x4800 - iVar26),
                            s___40s__0x_08X___40s__0x_08X___40_ffff0000907d7a8a,
                            s_MFG_RPC_GHPM_RO0_CON_ffff000090800b6f,_DAT_ffff00004b8009a4,
                            s_MFG_RPC_GHPM_RO1_CON_ffff0000907e93e6,_DAT_ffff00004b8009a8,
                            s_MFG_RPC_GHPM_RO2_CON_ffff0000907ee20b,uVar31);
        iVar27 = iVar27 + iVar26;
        uVar31 = CONCAT44((int)((ulong)uVar31 >> 0x20),_DAT_ffff00004b90004c);
        pcVar37 = s_MFG_VCORE_BUS_SYSTIMER_LATCH_L_ffff0000907f8208;
        uVar39 = _DAT_ffff00004b900048;
        iVar26 = FUN_ffff00009077f6fc
                           (lVar28 + iVar27,(long)(0x4800 - iVar27),
                            s___40s__0x_08X___40s__0x_08X___40_ffff0000907f9c6c,
                            s_MFG_VCORE_BUS_DBG_CON_ffff0000907fb849,_DAT_ffff00004b900000,
                            s_MFG_VCORE_BUS_TIMEOUT_INFO_ffff0000907cf75b,_DAT_ffff00004b900028,
                            s_MFG_VCORE_BUS_SYSTIMER_LATCH_H_ffff0000907f6467,uVar31,
                            s_MFG_VCORE_BUS_SYSTIMER_LATCH_L_ffff0000907f8208,_DAT_ffff00004b900048)
        ;
        lVar30 = 0;
        iVar26 = iVar26 + iVar27;
        do {
          if (*(int *)(lVar30 + -0xffffb46ffe00) != 0) {
            uVar31 = CONCAT44((int)((ulong)uVar31 >> 0x20),*(undefined4 *)(lVar30 + -0xffffb46ffc00)
                             );
            iVar27 = FUN_ffff00009077f6fc
                               (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                s___40s__0x_08X___40s__0x_08X___40_ffff0000907d7a8a,
                                s_MFG_VCORE_BUS_AR_TRACKER_LOG_ffff0000907ec586,
                                *(int *)(lVar30 + -0xffffb46ffe00),
                                s_MFG_VCORE_BUS_AR_TRACKER_ID_ffff0000907efafe,
                                *(undefined4 *)(lVar30 + -0xffffb46ffd00),
                                s_MFG_VCORE_BUS_AR_TRACKER_L_ffff0000907e1341,uVar31,pcVar37,uVar39)
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
            iVar27 = FUN_ffff00009077f6fc
                               (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                s___40s__0x_08X___40s__0x_08X___40_ffff0000907d7a8a,
                                s_MFG_VCORE_BUS_AW_TRACKER_LOG_ffff0000907fd416,
                                *(undefined4 *)(lVar30 + -0xffffb46ff800),
                                s_MFG_VCORE_BUS_AW_TRACKER_ID_ffff0000907e135c,
                                *(undefined4 *)(lVar30 + -0xffffb46ff700),
                                s_MFG_VCORE_BUS_AW_TRACKER_L_ffff0000907dabe8,uVar31,pcVar37,uVar39)
            ;
            iVar26 = iVar27 + iVar26;
          }
          uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
          lVar30 = lVar30 + 4;
        } while (lVar30 != 0x40);
        if (_DAT_ffff00004b90008c != 0) {
          uVar31 = CONCAT44(uVar38,_DAT_ffff00004b900080);
          iVar27 = FUN_ffff00009077f6fc
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___40s__0x_08X___40s__0x_08X___40_ffff0000907d7a8a,
                              s_MFG_VCORE_BUS_AR_SLVERR_LOG_ffff0000907d2c37,_DAT_ffff00004b90008c,
                              s_MFG_VCORE_BUS_AR_SLVERR_ID_ffff0000907cf776,_DAT_ffff00004b900088,
                              s_MFG_VCORE_BUS_AR_SLVERR_ADDR_L_ffff0000907d2c53,uVar31);
          uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
          iVar26 = iVar27 + iVar26;
        }
        if (_DAT_ffff00004b90009c != 0) {
          uVar31 = CONCAT44(uVar38,_DAT_ffff00004b900090);
          iVar27 = FUN_ffff00009077f6fc
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___40s__0x_08X___40s__0x_08X___40_ffff0000907d7a8a,
                              s_MFG_VCORE_BUS_AW_SLVERR_LOG_ffff0000907d61be,_DAT_ffff00004b90009c,
                              s_MFG_VCORE_BUS_AW_SLVERR_ID_ffff0000907f8227,_DAT_ffff00004b900098,
                              s_MFG_VCORE_BUS_AW_SLVERR_ADDR_L_ffff0000907e6220,uVar31);
          uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
          iVar26 = iVar27 + iVar26;
        }
        if (2 < _DAT_ffff00004b800504 >> 0x1e) {
          uVar31 = CONCAT44(uVar38,_DAT_ffff00004b1a004c);
          pcVar37 = s_MFG_GPUEB_BUS_SYSTIMER_LATCH_L_ffff0000907d45af;
          uVar39 = _DAT_ffff00004b1a0048;
          iVar27 = FUN_ffff00009077f6fc
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___40s__0x_08X___40s__0x_08X___40_ffff0000907f9c6c,
                              s_MFG_GPUEB_BUS_DBG_CON_ffff0000907df99d,_DAT_ffff00004b1a0000,
                              s_MFG_GPUEB_BUS_TIMEOUT_INFO_ffff0000907ec5a3,_DAT_ffff00004b1a0028,
                              s_MFG_GPUEB_BUS_SYSTIMER_LATCH_H_ffff0000907e623f,uVar31,
                              s_MFG_GPUEB_BUS_SYSTIMER_LATCH_L_ffff0000907d45af,
                              _DAT_ffff00004b1a0048);
          lVar30 = 0;
          iVar27 = iVar27 + iVar26;
          do {
            if (*(int *)(lVar30 + -0xffffb4e5fe00) != 0) {
              uVar31 = CONCAT44((int)((ulong)uVar31 >> 0x20),
                                *(undefined4 *)(lVar30 + -0xffffb4e5fc00));
              iVar26 = FUN_ffff00009077f6fc
                                 (lVar28 + iVar27,(long)(0x4800 - iVar27),
                                  s___40s__0x_08X___40s__0x_08X___40_ffff0000907d7a8a,
                                  s_MFG_GPUEB_BUS_AR_TRACKER_LOG_ffff0000907e4a30,
                                  *(int *)(lVar30 + -0xffffb4e5fe00),
                                  s_MFG_GPUEB_BUS_AR_TRACKER_ID_ffff0000907cf791,
                                  *(undefined4 *)(lVar30 + -0xffffb4e5fd00),
                                  s_MFG_GPUEB_BUS_AR_TRACKER_L_ffff0000907d92ac,uVar31,pcVar37,
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
              iVar26 = FUN_ffff00009077f6fc
                                 (lVar28 + iVar27,(long)(0x4800 - iVar27),
                                  s___40s__0x_08X___40s__0x_08X___40_ffff0000907d7a8a,
                                  s_MFG_GPUEB_BUS_AW_TRACKER_LOG_ffff000090800b84,
                                  *(undefined4 *)(lVar30 + -0xffffb4e5f800),
                                  s_MFG_GPUEB_BUS_AW_TRACKER_ID_ffff0000907e1378,
                                  *(undefined4 *)(lVar30 + -0xffffb4e5f700),
                                  s_MFG_GPUEB_BUS_AW_TRACKER_L_ffff0000907f8242,uVar31,pcVar37,
                                  uVar39);
              iVar27 = iVar26 + iVar27;
            }
            uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
            lVar30 = lVar30 + 4;
          } while (lVar30 != 0x80);
          if (_DAT_ffff00004b1a008c != 0) {
            uVar31 = CONCAT44(uVar38,_DAT_ffff00004b1a0080);
            iVar26 = FUN_ffff00009077f6fc
                               (lVar28 + iVar27,(long)(0x4800 - iVar27),
                                s___40s__0x_08X___40s__0x_08X___40_ffff0000907d7a8a,
                                s_MFG_GPUEB_BUS_AR_SLVERR_LOG_ffff0000907d113c,_DAT_ffff00004b1a008c
                                ,s_MFG_GPUEB_BUS_AR_SLVERR_ID_ffff0000907ec5be,_DAT_ffff00004b1a0088
                                ,s_MFG_GPUEB_BUS_AR_SLVERR_ADDR_L_ffff0000907f825d,uVar31);
            uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
            iVar27 = iVar26 + iVar27;
          }
          if (_DAT_ffff00004b1a009c != 0) {
            uVar31 = CONCAT44(uVar38,_DAT_ffff00004b1a0090);
            iVar26 = FUN_ffff00009077f6fc
                               (lVar28 + iVar27,(long)(0x4800 - iVar27),
                                s___40s__0x_08X___40s__0x_08X___40_ffff0000907d7a8a,
                                s_MFG_GPUEB_BUS_AW_SLVERR_LOG_ffff0000907f827c,_DAT_ffff00004b1a009c
                                ,s_MFG_GPUEB_BUS_AW_SLVERR_ID_ffff0000907d61da,_DAT_ffff00004b1a0098
                                ,s_MFG_GPUEB_BUS_AW_SLVERR_ADDR_L_ffff0000907e4a4d,uVar31);
            uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
            iVar27 = iVar26 + iVar27;
          }
          iVar26 = FUN_ffff00009077f6fc
                             (lVar28 + iVar27,(long)(0x4800 - iVar27),
                              s___40s__0x_08X_ffff0000907d45ce,
                              s_GPUFREQ_FOOTPRINT_GPR_ffff0000907e93fb,_DAT_ffff00004b09fd44);
          iVar26 = iVar26 + iVar27;
        }
        if (2 < _DAT_ffff00004b800500 >> 0x1e) {
          uVar31 = CONCAT44(uVar38,_DAT_ffff00004880004c);
          pcVar37 = s_MFG_VGPU_BUS_SYSTIMER_LATCH_L_ffff0000907f6486;
          uVar39 = _DAT_ffff000048800048;
          iVar27 = FUN_ffff00009077f6fc
                             (lVar28 + iVar26,(long)(0x4800 - iVar26),
                              s___40s__0x_08X___40s__0x_08X___40_ffff0000907f9c6c,
                              s_MFG_VGPU_BUS_DBG_CON_ffff0000907ead9d,_DAT_ffff000048800000,
                              s_MFG_VGPU_BUS_TIMEOUT_INFO_ffff0000907f9ca5,_DAT_ffff000048800028,
                              s_MFG_VGPU_BUS_SYSTIMER_LATCH_H_ffff0000907d61f5,uVar31,
                              s_MFG_VGPU_BUS_SYSTIMER_LATCH_L_ffff0000907f6486,_DAT_ffff000048800048
                             );
          lVar30 = 0;
          iVar26 = iVar27 + iVar26;
          do {
            if (*(int *)(lVar30 + -0xffffb77ffe00) != 0) {
              uVar31 = CONCAT44((int)((ulong)uVar31 >> 0x20),
                                *(undefined4 *)(lVar30 + -0xffffb77ffc00));
              iVar27 = FUN_ffff00009077f6fc
                                 (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                  s___40s__0x_08X___40s__0x_08X___40_ffff0000907d7a8a,
                                  s_MFG_VGPU_BUS_AR_TRACKER_LOG_ffff0000907e625e,
                                  *(int *)(lVar30 + -0xffffb77ffe00),
                                  s_MFG_VGPU_BUS_AR_TRACKER_ID_ffff0000907f4899,
                                  *(undefined4 *)(lVar30 + -0xffffb77ffd00),
                                  s_MFG_VGPU_BUS_AR_TRACKER_L_ffff0000907e2d29,uVar31,pcVar37,uVar39
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
              iVar27 = FUN_ffff00009077f6fc
                                 (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                  s___40s__0x_08X___40s__0x_08X___40_ffff0000907d7a8a,
                                  s_MFG_VGPU_BUS_AW_TRACKER_LOG_ffff0000907f2c74,
                                  *(undefined4 *)(lVar30 + -0xffffb77ff800),
                                  s_MFG_VGPU_BUS_AW_TRACKER_ID_ffff0000907e9411,
                                  *(undefined4 *)(lVar30 + -0xffffb77ff700),
                                  s_MFG_VGPU_BUS_AW_TRACKER_L_ffff0000907e942c,uVar31,pcVar37,uVar39
                                 );
              iVar26 = iVar27 + iVar26;
            }
            uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
            lVar30 = lVar30 + 4;
          } while (lVar30 != 0x80);
          if (_DAT_ffff00004880008c != 0) {
            uVar31 = CONCAT44(uVar38,_DAT_ffff000048800080);
            iVar27 = FUN_ffff00009077f6fc
                               (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                s___40s__0x_08X___40s__0x_08X___40_ffff0000907d7a8a,
                                s_MFG_VGPU_BUS_AR_SLVERR_LOG_ffff0000907e1394,_DAT_ffff00004880008c,
                                s_MFG_VGPU_BUS_AR_SLVERR_ID_ffff0000907e9446,_DAT_ffff000048800088,
                                s_MFG_VGPU_BUS_AR_SLVERR_ADDR_L_ffff0000907ee220,uVar31);
            uVar38 = (undefined4)((ulong)uVar31 >> 0x20);
            iVar26 = iVar27 + iVar26;
          }
          if (_DAT_ffff00004880009c != 0) {
            iVar27 = FUN_ffff00009077f6fc
                               (lVar28 + iVar26,(long)(0x4800 - iVar26),
                                s___40s__0x_08X___40s__0x_08X___40_ffff0000907d7a8a,
                                s_MFG_VGPU_BUS_AW_SLVERR_LOG_ffff0000907d92c7,_DAT_ffff00004880009c,
                                s_MFG_VGPU_BUS_AW_SLVERR_ID_ffff0000907e627a,_DAT_ffff000048800098,
                                s_MFG_VGPU_BUS_AW_SLVERR_ADDR_L_ffff0000907d1158,
                                CONCAT44(uVar38,_DAT_ffff000048800090));
            iVar26 = iVar27 + iVar26;
          }
        }
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__invalid_state_of_GPUEB_SHARE_ffff0000907e498f,
                   s__MFGSYS__EXPDB__ffff0000907de0f2);
        iVar26 = 0;
      }
      uVar31 = FUN_ffff00009077b334(lVar28);
      uVar35 = (*param_1)(uVar31,(long)iVar26);
      if ((uVar35 & 1) == 0) {
        FUN_ffff00009077e11c
                  (s__s_SYS_GPU_MFGSYS_LATCH_LOG_dump_ffff0000907df934,
                   s__MFGSYS__EXPDB__ffff0000907de0f2);
      }
      goto LAB_ffff00009073340c;
    }
  }
  FUN_ffff00009077e11c
            (s__s__invalid_GPUEB_SHARED__addr__0_ffff000090802688,s__MFGSYS__EXPDB__ffff0000907de0f2
             ,local_68,local_78);
LAB_ffff00009073340c:
  thunk_FUN_ffff0000907c314c(lVar28);
  return;
}


