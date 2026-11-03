// ffff00009079d83c  size=832  FUN_ffff00009079d83c

int FUN_ffff00009079d83c(char *param_1,char *param_2,ulong *param_3,undefined8 *param_4)

{
  ulong uVar1;
  ulong uVar2;
  uint uVar3;
  char cVar4;
  char cVar5;
  long lVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  int iVar9;
  ushort *puVar10;
  ushort *puVar11;
  uint uVar12;
  short *psVar13;
  int local_a0;
  uint uStack_9c;
  uint local_98;
  undefined4 uStack_94;
  short *local_90;
  undefined8 local_88;
  ulong local_80;
  short *local_78;
  
  local_80 = 0;
  local_78 = (short *)0x0;
  local_90 = (short *)0x0;
  local_88 = 0;
  local_a0 = 0;
  uStack_9c = 0;
  local_98 = 0;
  uStack_94 = 0;
  if ((param_1 == (char *)0x0) || (param_2 == (char *)0x0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e0593,0x4a7,
               s_a______void__0_____b______void___ffff0000907eef8c);
  }
  cVar4 = *param_2;
  uVar12 = *(uint *)(param_2 + 8);
  if ((cVar4 != '\x01') && (uVar12 == 1)) {
    if (**(short **)(param_2 + 0x10) == 0) {
      return -3;
    }
    uVar12 = 1;
  }
  uVar3 = *(uint *)(param_1 + 8);
  if (uVar12 > uVar3 || uVar3 == uVar12) {
    if (uVar12 <= uVar3) {
      if (-1 < (int)(uVar3 - 1)) {
        lVar6 = (ulong)(uVar3 - 1) * 2;
        puVar10 = (ushort *)(*(long *)(param_2 + 0x10) + lVar6);
        puVar11 = (ushort *)(*(long *)(param_1 + 0x10) + lVar6);
        uVar12 = uVar3;
        do {
          if (*puVar10 < *puVar11) goto LAB_ffff00009079d8b0;
          if (*puVar11 < *puVar10) goto LAB_ffff00009079d9e4;
          puVar11 = puVar11 + -1;
          puVar10 = puVar10 + -1;
          uVar12 = uVar12 - 1;
        } while (0 < (int)uVar12);
      }
      if (param_3 != (ulong *)0x0) {
        cVar5 = *param_1;
        FUN_ffff00009077eb88(param_3[2],0,(ulong)*(uint *)((long)param_3 + 4) << 1);
        *(undefined1 *)param_3 = 0;
        *(undefined4 *)(param_3 + 1) = 1;
        *(undefined2 *)param_3[2] = 1;
        if (cVar5 != cVar4) {
          *(undefined1 *)param_3 = 1;
        }
      }
      if (param_4 != (undefined8 *)0x0) {
        FUN_ffff00009077eb88(param_4[2],0,(ulong)*(uint *)((long)param_4 + 4) << 1);
        *(undefined1 *)param_4 = 0;
        *(undefined4 *)(param_4 + 1) = 1;
        return 0;
      }
    }
    else {
LAB_ffff00009079d9e4:
      if (param_4 != (undefined8 *)0x0) {
        iVar9 = FUN_ffff00009079bf14(param_1,param_4);
        if (iVar9 != 0) {
          return iVar9;
        }
      }
      if (param_3 != (ulong *)0x0) {
        FUN_ffff00009077eb88(param_3[2],0,(ulong)*(uint *)((long)param_3 + 4) << 1);
        *(undefined1 *)param_3 = 0;
        *(undefined4 *)(param_3 + 1) = 1;
        return 0;
      }
    }
    return 0;
  }
LAB_ffff00009079d8b0:
  if ((char *)&local_88 != param_1) {
    local_78 = (short *)FUN_ffff00009077d2e0(uVar3,2);
    if (local_78 == (short *)0x0) {
      return -2;
    }
    FUN_ffff00009077e858
              (local_78,*(undefined8 *)(param_1 + 0x10),(ulong)*(uint *)(param_1 + 8) << 1);
    local_88 = CONCAT44(*(undefined4 *)(param_1 + 8),(undefined4)local_88);
    local_80 = CONCAT44(local_80._4_4_,*(undefined4 *)(param_1 + 8));
    local_88 = CONCAT71(local_88._1_7_,*param_1);
  }
  if (&local_a0 != (int *)param_2) {
    local_90 = (short *)FUN_ffff00009077d2e0(*(undefined4 *)(param_2 + 8),2);
    if (local_90 == (short *)0x0) {
      iVar9 = -2;
      goto LAB_ffff00009079daf4;
    }
    FUN_ffff00009077e858
              (local_90,*(undefined8 *)(param_2 + 0x10),(ulong)*(uint *)(param_2 + 8) << 1);
    uStack_9c = *(uint *)(param_2 + 8);
    local_a0 = CONCAT31(local_a0._1_3_,*param_2);
    local_98 = uStack_9c;
  }
  iVar9 = FUN_ffff00009079db7c(&local_88,&local_a0);
  if (iVar9 == 0) {
    local_a0 = CONCAT31(local_a0._1_3_,*param_1);
    local_88 = CONCAT71(local_88._1_7_,*param_1 != *param_2);
    if (((uint)local_80 < 2) && (*local_78 == 0)) {
      local_88 = (ulong)local_88._1_7_ << 8;
    }
    if ((local_98 < 2) && (*local_90 == 0)) {
      local_a0 = (uint)local_a0._1_3_ << 8;
    }
    if (param_3 != (ulong *)0x0) {
      uVar1 = *param_3;
      uVar2 = param_3[1];
      psVar13 = (short *)param_3[2];
      *param_3 = local_88;
      param_3[1] = local_80;
      param_3[2] = (ulong)local_78;
      local_88 = uVar1;
      local_80 = uVar2;
      local_78 = psVar13;
    }
    iVar9 = 0;
    if (param_4 != (undefined8 *)0x0) {
      uVar7 = CONCAT44(uStack_9c,local_a0);
      uVar8 = CONCAT44(uStack_94,local_98);
      psVar13 = (short *)param_4[2];
      local_a0 = (int)*param_4;
      uStack_9c = (uint)((ulong)*param_4 >> 0x20);
      local_98 = (uint)param_4[1];
      uStack_94 = (undefined4)((ulong)param_4[1] >> 0x20);
      *param_4 = uVar7;
      param_4[1] = uVar8;
      param_4[2] = local_90;
      local_90 = psVar13;
    }
  }
LAB_ffff00009079daf4:
  if (local_90 != (short *)0x0) {
    FUN_ffff00009077eb88(local_90,0,(ulong)uStack_9c << 1);
    if (local_90 != (short *)0x0) {
      thunk_FUN_ffff0000907c2ad8();
    }
    local_90 = (short *)0x0;
  }
  uStack_9c = 0;
  local_98 = 0;
  if ((local_78 != (short *)0x0) &&
     (FUN_ffff00009077eb88(local_78,0,(local_88 >> 0x20) << 1), local_78 != (short *)0x0)) {
    thunk_FUN_ffff0000907c2ad8();
  }
  return iVar9;
}


