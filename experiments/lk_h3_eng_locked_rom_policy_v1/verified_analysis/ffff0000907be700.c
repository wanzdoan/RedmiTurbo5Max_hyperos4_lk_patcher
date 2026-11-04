
/* WARNING: Type propagation algorithm not settling */

uint FUN_ffff0000907be700
               (long param_1,long *param_2,undefined8 param_3,uint param_4,int param_5,long *param_6
               )

{
  bool bVar1;
  uint uVar2;
  int iVar3;
  uint uVar4;
  long *plVar5;
  long lVar6;
  undefined8 uVar7;
  long lVar8;
  ulong uVar9;
  undefined *puVar10;
  char *pcVar11;
  long lVar12;
  undefined4 uVar13;
  int local_184;
  undefined8 local_180;
  long local_178 [16];
  undefined8 local_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined4 local_6c [3];

  local_6c[0] = 0;
  local_180 = 0;
  local_178[0] = 0;
  if (param_6 != (long *)0x0) {
    *param_6 = 0;
  }
  if ((param_5 == 3) && ((param_4 & 1) == 0)) {
    return 8;
  }
  if ((param_5 == 4) && ((*(long *)(param_1 + 0x60) == 0 || (*(long *)(param_1 + 0x68) == 0)))) {
    uVar7 = FUN_ffff0000907c119c(s_external_lib_libavb_avb_slot_ver_ffff0000907fab02);
    puVar10 = &DAT_ffff0000907c87a8;
    pcVar11 = s_Persistent_values_required_for_A_ffff0000907fab28;
LAB_ffff0000907be980:
    FUN_ffff0000907c0ea8(uVar7,&DAT_ffff0000907e4abf,puVar10,s___ERROR__ffff0000907d8dec,pcVar11,0);
    return 8;
  }
  if (((param_4 >> 2 & 1) != 0) && (*(long *)(param_1 + 0x70) == 0)) {
    uVar7 = FUN_ffff0000907c119c(s_external_lib_libavb_avb_slot_ver_ffff0000907fab02);
    puVar10 = &DAT_ffff0000907cd71b;
    pcVar11 = s_AVB_SLOT_VERIFY_FLAGS_NO_VBMETA__ffff0000907d76ca;
    goto LAB_ffff0000907be980;
  }
  plVar5 = (long *)FUN_ffff0000907c159c(0x138);
  if (plVar5 == (long *)0x0) {
    return 1;
  }
  local_178[0xd] = 0;
  local_178[0xe] = 0;
  local_178[0xb] = 0;
  local_178[0xc] = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  uStack_e8 = 0;
  local_178[0xf] = 0;
  local_f8 = 0;
  local_178[9] = 0;
  local_178[10] = 0;
  local_178[7] = 0;
  local_178[8] = 0;
  local_178[5] = 0;
  local_178[6] = 0;
  local_178[3] = 0;
  local_178[4] = 0;
  local_178[1] = 0;
  local_178[2] = 0;
  lVar6 = FUN_ffff0000907c159c(0x400);
  plVar5[1] = lVar6;
  if (lVar6 == 0) {
LAB_ffff0000907be920:
    lVar6 = 0;
LAB_ffff0000907be924:
    uVar4 = 1;
  }
  else {
    lVar6 = FUN_ffff0000907c159c(0x400);
    plVar5[3] = lVar6;
    if (lVar6 == 0) goto LAB_ffff0000907be920;
    plVar5[6] = 0xffffffff;
    plVar5[7] = 0xffffffff;
    plVar5[8] = 0xffffffff;
    plVar5[9] = 0xffffffff;
    plVar5[10] = 0xffffffff;
    plVar5[0xb] = 0xffffffff;
    plVar5[0xc] = 0xffffffff;
    plVar5[0xd] = 0xffffffff;
    plVar5[0xe] = 0xffffffff;
    plVar5[0xf] = 0xffffffff;
    plVar5[0x10] = 0xffffffff;
    plVar5[0x11] = 0xffffffff;
    plVar5[0x12] = 0xffffffff;
    plVar5[0x13] = 0xffffffff;
    plVar5[0x14] = 0xffffffff;
    plVar5[0x15] = 0xffffffff;
    plVar5[0x16] = 0xffffffff;
    plVar5[0x17] = 0xffffffff;
    plVar5[0x18] = 0xffffffff;
    plVar5[0x19] = 0xffffffff;
    plVar5[0x1a] = 0xffffffff;
    plVar5[0x1b] = 0xffffffff;
    plVar5[0x1c] = 0xffffffff;
    plVar5[0x1d] = 0xffffffff;
    plVar5[0x1e] = 0xffffffff;
    plVar5[0x1f] = 0xffffffff;
    plVar5[0x20] = 0xffffffff;
    plVar5[0x21] = 0xffffffff;
    plVar5[0x22] = 0xffffffff;
    plVar5[0x23] = 0xffffffff;
    plVar5[0x24] = 0xffffffff;
    plVar5[0x25] = 0xffffffff;
    lVar6 = FUN_ffff0000907bcc58();
    if (lVar6 == 0) goto LAB_ffff0000907be924;
    if ((param_4 >> 2 & 1) != 0) {
      if ((param_2 == (long *)0x0) || (lVar8 = *param_2, lVar8 == 0)) {
        uVar7 = FUN_ffff0000907c119c(s_external_lib_libavb_avb_slot_ver_ffff0000907fab02);
        FUN_ffff0000907c0ea8
                  (uVar7,&DAT_ffff0000907e4abf,&DAT_ffff0000907ec9f7,s___FATAL__ffff0000907ee78e,
                   s_Requested_partitions_cannot_be_e_ffff0000907f0463,0);
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000907c0ea4();
      }
      lVar12 = 8;
      do {
        uVar7 = thunk_FUN_ffff00009077fb34(lVar8);
        uVar2 = FUN_ffff0000907beba0
                          (param_1,param_2,param_3,param_4,param_4 & 1,0,0,lVar8,uVar7,0,0,plVar5,
                           local_6c,0,0,lVar6);
        uVar13 = (undefined4)((ulong)uVar7 >> 0x20);
        if (((param_4 & 1) == 0) && (uVar4 = uVar2, uVar2 != 0)) goto LAB_ffff0000907be928;
        lVar8 = *(long *)((long)param_2 + lVar12);
        lVar12 = lVar12 + 8;
      } while (lVar8 != 0);
LAB_ffff0000907be8ac:
      uVar4 = uVar2;
      if ((5 < uVar2) || ((1 << (ulong)(uVar2 & 0x1f) & 0x39U) == 0)) goto LAB_ffff0000907be928;
      if ((param_4 >> 2 & 1) == 0) {
        iVar3 = thunk_FUN_ffff00009077f990(*(undefined8 *)plVar5[1],s_vbmeta_ffff0000907d2919);
        bVar1 = iVar3 != 0;
      }
      else {
        bVar1 = false;
      }
      FUN_ffff0000907c268c(*(undefined8 *)(plVar5[1] + 8),local_178 + 1);
      lVar8 = FUN_ffff0000907c1650(param_3);
      *plVar5 = lVar8;
      if (lVar8 != 0) {
        if (((byte)local_f8 >> 1 & 1) == 0) {
          local_184 = param_5;
          if ((param_5 == 4) &&
             (iVar3 = FUN_ffff0000907bfc08(param_1,plVar5,&local_184), iVar3 != 0)) {
            uVar4 = 1;
            if (iVar3 != 1) {
              uVar4 = 2;
            }
          }
          else {
            *(int *)(plVar5 + 0x26) = local_184;
            uVar4 = FUN_ffff0000907bc594
                              (param_1,param_4,plVar5,local_178 + 1,local_178[0],local_180,
                               local_6c[0],param_5,CONCAT44(uVar13,local_184));
            if (uVar4 == 0) {
              if (plVar5[5] != 0) goto LAB_ffff0000907beb04;
              goto LAB_ffff0000907beb44;
            }
          }
          goto LAB_ffff0000907be928;
        }
        uVar9 = FUN_ffff0000907bfaec(param_1,param_3);
        pcVar11 = s_root_PARTUUID___ANDROID_SYSTEM_P_ffff0000907d8e3f;
        if ((uVar9 & 1) == 0) {
          pcVar11 = &DAT_ffff0000907d788e;
        }
        lVar8 = FUN_ffff0000907c1650(pcVar11);
        plVar5[5] = lVar8;
        if (lVar8 != 0) {
LAB_ffff0000907beb04:
          lVar8 = thunk_FUN_ffff00009077fb34();
          if (lVar8 != 0) {
            lVar8 = FUN_ffff0000907bc208(param_1,plVar5[5],param_3,bVar1,lVar6);
            if (lVar8 != plVar5[5]) {
              if (lVar8 == 0) goto LAB_ffff0000907be924;
              FUN_ffff0000907c0fd4(plVar5[5]);
              plVar5[5] = lVar8;
            }
          }
LAB_ffff0000907beb44:
          if (param_6 == (long *)0x0) {
            FUN_ffff0000907bff28(plVar5);
          }
          else {
            *param_6 = (long)plVar5;
          }
          if (local_178[0] != 0) {
            FUN_ffff0000907c0fd4();
          }
          goto LAB_ffff0000907be940;
        }
      }
      goto LAB_ffff0000907be924;
    }
    uVar7 = thunk_FUN_ffff00009077fb34(s_vbmeta_ffff0000907d2919);
    uVar2 = FUN_ffff0000907beba0
                      (param_1,param_2,param_3,param_4,param_4 & 1,0,0,s_vbmeta_ffff0000907d2919,
                       uVar7,0,0,plVar5,local_6c,local_178,&local_180,lVar6);
    uVar13 = (undefined4)((ulong)uVar7 >> 0x20);
    if (((param_4 & 1) != 0) || (uVar4 = uVar2, uVar2 == 0)) goto LAB_ffff0000907be8ac;
  }
LAB_ffff0000907be928:
  FUN_ffff0000907bff28(plVar5);
  if (local_178[0] != 0) {
    FUN_ffff0000907c0fd4();
  }
  uVar2 = uVar4;
  if (lVar6 == 0) {
    return uVar4;
  }
LAB_ffff0000907be940:
  FUN_ffff0000907bcc68(lVar6);
  return uVar2;
}
