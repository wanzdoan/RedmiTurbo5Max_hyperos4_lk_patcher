// ffff0000907be0cc  size=1184  FUN_ffff0000907be0cc

/* WARNING: Type propagation algorithm not settling */

uint FUN_ffff0000907be0cc
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
    uVar7 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    puVar10 = &DAT_ffff0000907d06d8;
    pcVar11 = s_Persistent_values_required_for_A_ffff0000908019c9;
LAB_ffff0000907be34c:
    FUN_ffff0000907c0874(uVar7,&DAT_ffff0000907ebe0c,puVar10,s___ERROR__ffff0000907e0722,pcVar11,0);
    return 8;
  }
  if (((param_4 >> 2 & 1) != 0) && (*(long *)(param_1 + 0x70) == 0)) {
    uVar7 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    puVar10 = &DAT_ffff0000907d54d9;
    pcVar11 = s_AVB_SLOT_VERIFY_FLAGS_NO_VBMETA__ffff0000907df08c;
    goto LAB_ffff0000907be34c;
  }
  plVar5 = (long *)FUN_ffff0000907c0f68(0x138);
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
  lVar6 = FUN_ffff0000907c0f68(0x400);
  plVar5[1] = lVar6;
  if (lVar6 == 0) {
LAB_ffff0000907be2ec:
    lVar6 = 0;
LAB_ffff0000907be2f0:
    uVar4 = 1;
  }
  else {
    lVar6 = FUN_ffff0000907c0f68(0x400);
    plVar5[3] = lVar6;
    if (lVar6 == 0) goto LAB_ffff0000907be2ec;
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
    lVar6 = FUN_ffff0000907bc624();
    if (lVar6 == 0) goto LAB_ffff0000907be2f0;
    if ((param_4 >> 2 & 1) != 0) {
      if ((param_2 == (long *)0x0) || (lVar8 = *param_2, lVar8 == 0)) {
        uVar7 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
        FUN_ffff0000907c0874
                  (uVar7,&DAT_ffff0000907ebe0c,&DAT_ffff0000907f3b75,s___FATAL__ffff0000907f598b,
                   s_Requested_partitions_cannot_be_e_ffff0000907f75d5,0);
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000907c0870();
      }
      lVar12 = 8;
      do {
        uVar7 = thunk_FUN_ffff00009077ef88(lVar8);
        uVar2 = FUN_ffff0000907be56c
                          (param_1,param_2,param_3,param_4,param_4 & 1,0,0,lVar8,uVar7,0,0,plVar5,
                           local_6c,0,0,lVar6);
        uVar13 = (undefined4)((ulong)uVar7 >> 0x20);
        if (((param_4 & 1) == 0) && (uVar4 = uVar2, uVar2 != 0)) goto LAB_ffff0000907be2f4;
        lVar8 = *(long *)((long)param_2 + lVar12);
        lVar12 = lVar12 + 8;
      } while (lVar8 != 0);
LAB_ffff0000907be278:
      uVar4 = uVar2;
      if ((5 < uVar2) || ((1 << (ulong)(uVar2 & 0x1f) & 0x39U) == 0)) goto LAB_ffff0000907be2f4;
      if ((param_4 >> 2 & 1) == 0) {
        iVar3 = thunk_FUN_ffff00009077ede4(*(undefined8 *)plVar5[1],s_vbmeta_ffff0000907da492);
        bVar1 = iVar3 != 0;
      }
      else {
        bVar1 = false;
      }
      FUN_ffff0000907c2058(*(undefined8 *)(plVar5[1] + 8),local_178 + 1);
      lVar8 = FUN_ffff0000907c101c(param_3);
      *plVar5 = lVar8;
      if (lVar8 != 0) {
        if (((byte)local_f8 >> 1 & 1) == 0) {
          local_184 = param_5;
          if ((param_5 == 4) &&
             (iVar3 = FUN_ffff0000907bf5d4(param_1,plVar5,&local_184), iVar3 != 0)) {
            uVar4 = 1;
            if (iVar3 != 1) {
              uVar4 = 2;
            }
          }
          else {
            *(int *)(plVar5 + 0x26) = local_184;
            uVar4 = FUN_ffff0000907bbf60
                              (param_1,param_4,plVar5,local_178 + 1,local_178[0],local_180,
                               local_6c[0],param_5,CONCAT44(uVar13,local_184));
            if (uVar4 == 0) {
              if (plVar5[5] != 0) goto LAB_ffff0000907be4d0;
              goto LAB_ffff0000907be510;
            }
          }
          goto LAB_ffff0000907be2f4;
        }
        uVar9 = FUN_ffff0000907bf4b8(param_1,param_3);
        pcVar11 = s_root_PARTUUID___ANDROID_SYSTEM_P_ffff0000907e0775;
        if ((uVar9 & 1) == 0) {
          pcVar11 = &DAT_ffff0000907df27e;
        }
        lVar8 = FUN_ffff0000907c101c(pcVar11);
        plVar5[5] = lVar8;
        if (lVar8 != 0) {
LAB_ffff0000907be4d0:
          lVar8 = thunk_FUN_ffff00009077ef88();
          if (lVar8 != 0) {
            lVar8 = FUN_ffff0000907bbbd4(param_1,plVar5[5],param_3,bVar1,lVar6);
            if (lVar8 != plVar5[5]) {
              if (lVar8 == 0) goto LAB_ffff0000907be2f0;
              FUN_ffff0000907c09a0(plVar5[5]);
              plVar5[5] = lVar8;
            }
          }
LAB_ffff0000907be510:
          if (param_6 == (long *)0x0) {
            FUN_ffff0000907bf8f4(plVar5);
          }
          else {
            *param_6 = (long)plVar5;
          }
          if (local_178[0] != 0) {
            FUN_ffff0000907c09a0();
          }
          goto LAB_ffff0000907be30c;
        }
      }
      goto LAB_ffff0000907be2f0;
    }
    uVar7 = thunk_FUN_ffff00009077ef88(s_vbmeta_ffff0000907da492);
    uVar2 = FUN_ffff0000907be56c
                      (param_1,param_2,param_3,param_4,param_4 & 1,0,0,s_vbmeta_ffff0000907da492,
                       uVar7,0,0,plVar5,local_6c,local_178,&local_180,lVar6);
    uVar13 = (undefined4)((ulong)uVar7 >> 0x20);
    if (((param_4 & 1) != 0) || (uVar4 = uVar2, uVar2 == 0)) goto LAB_ffff0000907be278;
  }
LAB_ffff0000907be2f4:
  FUN_ffff0000907bf8f4(plVar5);
  if (local_178[0] != 0) {
    FUN_ffff0000907c09a0();
  }
  uVar2 = uVar4;
  if (lVar6 == 0) {
    return uVar4;
  }
LAB_ffff0000907be30c:
  FUN_ffff0000907bc634(lVar6);
  return uVar2;
}


