// ffff00009079e4b8  size=888  FUN_ffff00009079e4b8

int FUN_ffff00009079e4b8(undefined4 *param_1,char *param_2,char *param_3,ulong *param_4)

{
  ulong uVar1;
  int iVar2;
  int iVar3;
  undefined2 *puVar4;
  uint uVar5;
  int iVar6;
  ulong uVar7;
  ushort *puVar8;
  ushort *puVar9;
  long lVar10;
  ulong local_c0;
  undefined8 uStack_b8;
  long local_b0;
  undefined4 local_a8;
  uint local_a4;
  uint uStack_a0;
  undefined4 uStack_9c;
  long local_98;
  ulong local_90;
  ulong local_88;
  undefined2 *local_80;
  
  uVar5 = DAT_ffff000090837c58;
  local_90 = 0;
  local_88 = 0;
  uStack_a0 = 0;
  uStack_9c = 0;
  local_98 = 0;
  local_b0 = 0;
  local_a8 = 0;
  local_a4 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  if (((param_1 == (undefined4 *)0x0) || (param_2 == (char *)0x0)) || (param_4 == (ulong *)0x0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e0593,0x69e,
               s_a______void__0_____b______void___ffff0000907d6f3c);
  }
  if ((*param_2 == '\x01') || (*param_3 == '\x01')) {
    return -3;
  }
  puVar8 = *(ushort **)(param_2 + 0x10);
  iVar2 = *(int *)(param_2 + 8);
  if ((*(int *)(param_3 + 8) == 1) && (**(short **)(param_3 + 0x10) == 0)) {
    return -3;
  }
  uVar7 = (ulong)DAT_ffff000090837c58;
  if (DAT_ffff000090837c58 == 0) {
LAB_ffff00009079e7f0:
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e0593,0x137,
               s_mp______void__0_____prec_>_0_ffff0000907ea1fc);
  }
  puVar4 = (undefined2 *)FUN_ffff00009077d2e0(uVar7,2);
  if (puVar4 == (undefined2 *)0x0) {
    return -2;
  }
  local_90 = CONCAT44(uVar5,(int)local_90) & 0xffffffffffffff00;
  local_88 = CONCAT44(local_88._4_4_,1);
  local_80 = puVar4;
  if (&local_a8 != param_1) {
    local_98 = FUN_ffff00009077d2e0(param_1[2],2);
    if (local_98 == 0) {
      iVar3 = -2;
      goto LAB_ffff00009079e7b4;
    }
    FUN_ffff00009077e858(local_98,*(undefined8 *)(param_1 + 4),(ulong)(uint)param_1[2] << 1);
    local_a4 = param_1[2];
    local_a8 = CONCAT31(local_a8._1_3_,*(undefined1 *)param_1);
    uStack_a0 = local_a4;
  }
  iVar3 = FUN_ffff00009079e374(&local_a8,param_3,&local_a8);
  uVar5 = DAT_ffff000090837c58;
  if (iVar3 == 0) {
    if (DAT_ffff000090837c58 == 0) goto LAB_ffff00009079e7f0;
    local_b0 = FUN_ffff00009077d2e0(DAT_ffff000090837c58,2);
    if (local_b0 == 0) {
      iVar3 = -2;
    }
    else {
      local_c0 = CONCAT44(uVar5,(int)local_c0) & 0xffffffffffffff00;
      uStack_b8 = CONCAT44(uStack_b8._4_4_,1);
      FUN_ffff00009077eb88(puVar4,0,uVar7 << 1);
      local_88 = CONCAT44(local_88._4_4_,1);
      local_90 = local_90 & 0xffffffffffffff00;
      *puVar4 = 1;
      iVar3 = FUN_ffff00009079c258(&local_c0,1);
      if (((iVar3 == 0) &&
          (iVar3 = FUN_ffff00009079e830(&local_c0,*(int *)(param_3 + 8) << 1), iVar3 == 0)) &&
         (iVar3 = FUN_ffff00009079d83c(&local_c0,param_3,&local_c0,0), iVar3 == 0)) {
        puVar9 = puVar8;
        if (iVar2 != 1) {
          iVar6 = 0;
          puVar9 = puVar8 + (ulong)(iVar2 - 2) + 1;
          do {
            uVar7 = (ulong)*puVar8;
            lVar10 = 0x10;
            do {
              if (((((uVar7 & 1) != 0) &&
                   ((iVar3 = FUN_ffff00009079c824(&local_90,&local_a8), iVar3 != 0 ||
                    (iVar3 = FUN_ffff00009079e91c(&local_90,param_3,&local_c0), iVar3 != 0)))) ||
                  (iVar3 = FUN_ffff00009079c97c(&local_a8), iVar3 != 0)) ||
                 (iVar3 = FUN_ffff00009079e91c(&local_a8,param_3,&local_c0), iVar3 != 0))
              goto LAB_ffff00009079e778;
              uVar7 = uVar7 >> 1;
              lVar10 = lVar10 + -1;
            } while (lVar10 != 0);
            iVar6 = iVar6 + 1;
            puVar8 = puVar8 + 1;
          } while (iVar6 != iVar2 + -1);
        }
        uVar5 = (uint)*puVar9;
        while (uVar5 != 0) {
          if ((((uVar5 & 1) != 0) &&
              ((iVar3 = FUN_ffff00009079c824(&local_90,&local_a8), iVar3 != 0 ||
               (iVar3 = FUN_ffff00009079e91c(&local_90,param_3,&local_c0), iVar3 != 0)))) ||
             (iVar3 = FUN_ffff00009079c97c(&local_a8), iVar3 != 0)) goto LAB_ffff00009079e778;
          uVar5 = uVar5 >> 1;
          iVar3 = FUN_ffff00009079e91c(&local_a8,param_3,&local_c0);
          if (iVar3 != 0) goto LAB_ffff00009079e778;
        }
        uVar7 = *param_4;
        uVar1 = param_4[1];
        iVar3 = 0;
        puVar4 = (undefined2 *)param_4[2];
        *param_4 = local_90;
        param_4[1] = local_88;
        param_4[2] = (ulong)local_80;
        local_90 = uVar7;
        local_88 = uVar1;
        local_80 = puVar4;
      }
LAB_ffff00009079e778:
      FUN_ffff00009079bebc(&local_c0);
    }
  }
  if (local_98 != 0) {
    FUN_ffff00009077eb88(local_98,0,(ulong)local_a4 << 1);
    if (local_98 != 0) {
      thunk_FUN_ffff0000907c2ad8();
    }
    local_98 = 0;
  }
  local_a4 = 0;
  uStack_a0 = 0;
  puVar4 = local_80;
  if (local_80 == (undefined2 *)0x0) {
    return iVar3;
  }
LAB_ffff00009079e7b4:
  FUN_ffff00009077eb88(puVar4,0,(local_90 >> 0x20) << 1);
  if (local_80 != (undefined2 *)0x0) {
    thunk_FUN_ffff0000907c2ad8();
    return iVar3;
  }
  return iVar3;
}


