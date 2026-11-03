
int FUN_ffff0000907b887c(long *param_1)

{
  undefined4 uVar1;
  ulong uVar2;
  ulong uVar3;
  long lVar4;
  ulong uVar5;
  int iVar6;
  uint *puVar7;
  long lVar8;
  int iVar9;
  uint uVar10;
  uint uVar11;
  undefined8 uVar12;
  long lVar13;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  uint local_7c;
  undefined8 local_78;
  uint local_70;
  uint local_6c;
  uint local_68;
  uint local_64;
  uint local_48;
  int local_44;
  
  local_44 = 0xff;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  if (*(uint *)((long)param_1 + 0x44) < 3) {
    uVar5 = param_1[6];
    if (uVar5 == 0) {
      local_44 = 0x726f;
    }
    else {
      uVar12 = *(undefined8 *)
                (&DAT_ffff0000908135a8 + (long)(int)*(uint *)((long)param_1 + 0x44) * 8);
      local_48 = 0;
      if (0xf < uVar5) {
        uVar5 = 0x10;
      }
      local_78 = 0;
      local_7c = 0;
      FUN_ffff00009077ee50(&local_48,0,4);
      FUN_ffff00009077ee50((long)&local_78 + 4,0,4);
      FUN_ffff00009077ee50(&local_78,0,4);
      FUN_ffff00009077ee50(&local_7c,0,4);
      uVar2 = thunk_FUN_ffff00009077b334(&local_c0);
      uVar3 = thunk_FUN_ffff00009077b334((ulong)&local_c0 | 0x10);
      lVar13 = *param_1;
      uVar10 = *(int *)((long)param_1 + 0x44) << 0x1e;
      if (param_1[7] == 0) {
        local_48 = local_48 & 0x7807ff0 | uVar10 | 0x100a8000 | *(byte *)(param_1 + 8) & 0xf;
      }
      else {
        local_48 = local_48 & 0x7807fff | uVar10 | 0x100a8000;
        thunk_FUN_ffff000090707c50(param_1[7],uVar12);
        local_48 = local_48 | 0x80;
      }
      uVar10 = *(int *)(lVar13 + 0x54) + 1;
      uVar11 = uVar10 & 0xffff;
      if ((uVar10 & 0xfffe) == 0) {
        uVar11 = 1;
      }
      uVar11 = uVar11 | local_78._4_4_ & 0xf8ff0000;
      uVar1 = (undefined4)local_78;
      local_78 = CONCAT44(uVar11,(undefined4)local_78) | 0x300000000000000;
      if ((uVar3 & 0xf00000000) != 0 || (uVar2 >> 0x20 & 0xf) != 0) {
        local_7c = local_7c | 0x20;
        local_78 = CONCAT44(uVar11,uVar1) | 0x340000000000000;
      }
      thunk_FUN_ffff000090707c50(&local_c0,0x40);
      lVar4 = *param_1;
      do {
      } while ((*(uint *)(lVar4 + 0x100) & 0xf0) == 0);
      *(uint *)(lVar4 + 0x104) = local_48;
      *(uint *)(lVar4 + 0x104) = local_78._4_4_;
      *(undefined4 *)(lVar4 + 0x104) = (undefined4)local_78;
      if ((local_78._6_1_ >> 6 & 1) == 0) {
        iVar6 = 7;
      }
      else {
        iVar6 = 8;
        *(uint *)(lVar4 + 0x104) = local_7c;
      }
      *(int *)(lVar4 + 0x104) = (int)uVar2;
      *(undefined4 *)(lVar4 + 0x104) = 0x10;
      *(int *)(lVar4 + 0x104) = (int)uVar3;
      *(int *)(lVar4 + 0x104) = (int)uVar3 + 0xf;
      if ((local_48 >> 7 & 1) != 0) {
        uVar10 = 0;
        puVar7 = (uint *)param_1[7];
        iVar9 = 4;
        if ((local_48 >> 0x1e != 0) && ((local_48 & 0x780000) == 0x80000)) {
          if (local_48 >> 0x1e == 2) {
            uVar11 = 0;
            uVar10 = 0;
            *(undefined4 *)(lVar4 + 0x104) = 0;
          }
          else {
            uVar10 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
            *(uint *)(lVar4 + 0x104) = uVar10 >> 0x10 | uVar10 << 0x10;
            uVar10 = (puVar7[1] & 0xff00ff00) >> 8 | (puVar7[1] & 0xff00ff) << 8;
            uVar11 = uVar10 >> 0x10 | uVar10 << 0x10;
            uVar10 = 8;
          }
          *(uint *)(lVar4 + 0x104) = uVar11;
          uVar11 = *(uint *)((long)puVar7 + (ulong)uVar10);
          uVar11 = (uVar11 & 0xff00ff00) >> 8 | (uVar11 & 0xff00ff) << 8;
          *(uint *)(lVar4 + 0x104) = uVar11 >> 0x10 | uVar11 << 0x10;
          uVar11 = *(uint *)((long)puVar7 + ((ulong)uVar10 | 4));
          uVar11 = (uVar11 & 0xff00ff00) >> 8 | (uVar11 & 0xff00ff) << 8;
          uVar10 = uVar10 + 8;
          iVar9 = 8;
          *(uint *)(lVar4 + 0x104) = uVar11 >> 0x10 | uVar11 << 0x10;
        }
        puVar7 = (uint *)((long)puVar7 + (ulong)uVar10);
        iVar6 = iVar9 + iVar6;
        uVar10 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
        *(uint *)(lVar4 + 0x104) = uVar10 >> 0x10 | uVar10 << 0x10;
        uVar10 = (puVar7[1] & 0xff00ff00) >> 8 | (puVar7[1] & 0xff00ff) << 8;
        *(uint *)(lVar4 + 0x104) = uVar10 >> 0x10 | uVar10 << 0x10;
        uVar10 = (puVar7[2] & 0xff00ff00) >> 8 | (puVar7[2] & 0xff00ff) << 8;
        *(uint *)(lVar4 + 0x104) = uVar10 >> 0x10 | uVar10 << 0x10;
        uVar10 = (puVar7[3] & 0xff00ff00) >> 8 | (puVar7[3] & 0xff00ff) << 8;
        *(uint *)(lVar4 + 0x104) = uVar10 >> 0x10 | uVar10 << 0x10;
      }
      if (((local_78._6_1_ >> 6 & 1) != 0) && (((byte)local_7c >> 5 & 1) != 0)) {
        iVar6 = iVar6 + 7;
        *(uint *)(lVar4 + 0x104) = (uint)(uVar2 >> 0x20) & 0xf;
        *(undefined4 *)(lVar4 + 0x104) = 0;
        *(uint *)(lVar4 + 0x104) = (uint)(uVar3 >> 0x20) & 0xf;
        *(uint *)(lVar4 + 0x104) = (uint)(uVar3 + 0xf >> 0x20) & 0xf;
        *(undefined4 *)(lVar4 + 0x104) = 0;
        *(undefined4 *)(lVar4 + 0x104) = 0;
        *(undefined4 *)(lVar4 + 0x104) = 0;
      }
      *(uint *)(lVar4 + 0x100) = (*(uint *)(lVar4 + 0x100) & 0xff) - iVar6;
      local_44 = FUN_ffff0000907b8f14(lVar4,0,local_78._4_2_);
      if (local_44 == 0) {
        FUN_ffff00009077ee50(&local_48,0,4);
        FUN_ffff00009077ee50((long)&local_78 + 4,0,4);
        FUN_ffff00009077ee50(&local_78,0,4);
        FUN_ffff00009077ee50(&local_7c,0,4);
        uVar2 = thunk_FUN_ffff00009077b334(param_1[1]);
        lVar4 = thunk_FUN_ffff00009077b334(param_1[5]);
        uVar10 = *(int *)((long)param_1 + 0x44) << 0x1e;
        if (param_1[7] == 0) {
          local_48 = local_48 & 0x7807ff0 | uVar10 | 0x100b0000 | *(byte *)(param_1 + 8) & 0xf;
        }
        else {
          local_48 = local_48 & 0x7807fff | uVar10 | 0x100b0000;
          thunk_FUN_ffff000090707c50(param_1[7],uVar12);
          local_48 = local_48 | 0x80;
        }
        uVar10 = *(int *)(lVar13 + 0x54) + 1;
        uVar11 = uVar10 & 0xffff;
        if ((uVar10 & 0xfffe) == 0) {
          uVar11 = 1;
        }
        uVar11 = uVar11 | local_78._4_4_ & 0xf8ff0000;
        uVar1 = (undefined4)local_78;
        local_78 = CONCAT44(uVar11,(undefined4)local_78) | 0x300000000000000;
        if ((param_1[2] & 0xf00000000U) != 0 || (uVar2 >> 0x20 & 0xf) != 0) {
          local_7c = local_7c | 0x20;
          local_78 = CONCAT44(uVar11,uVar1) | 0x340000000000000;
        }
        thunk_FUN_ffff000090707c50(param_1[1]);
        lVar13 = *param_1;
        do {
        } while ((*(uint *)(lVar13 + 0x100) & 0xf0) == 0);
        lVar8 = param_1[2];
        *(uint *)(lVar13 + 0x104) = local_48;
        *(uint *)(lVar13 + 0x104) = local_78._4_4_;
        *(undefined4 *)(lVar13 + 0x104) = (undefined4)local_78;
        if ((local_78._6_1_ >> 6 & 1) == 0) {
          iVar6 = 7;
        }
        else {
          iVar6 = 8;
          *(uint *)(lVar13 + 0x104) = local_7c;
        }
        *(int *)(lVar13 + 0x104) = (int)uVar2;
        *(int *)(lVar13 + 0x104) = (int)lVar8;
        *(int *)(lVar13 + 0x104) = (int)lVar4;
        *(int *)(lVar13 + 0x104) = (int)(lVar4 + uVar5) + -1;
        if ((local_48 >> 7 & 1) != 0) {
          uVar10 = 0;
          puVar7 = (uint *)param_1[7];
          iVar9 = 4;
          if ((local_48 >> 0x1e != 0) && ((local_48 & 0x780000) == 0x80000)) {
            if (local_48 >> 0x1e == 2) {
              uVar11 = 0;
              uVar10 = 0;
              *(undefined4 *)(lVar13 + 0x104) = 0;
            }
            else {
              uVar10 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
              *(uint *)(lVar13 + 0x104) = uVar10 >> 0x10 | uVar10 << 0x10;
              uVar10 = (puVar7[1] & 0xff00ff00) >> 8 | (puVar7[1] & 0xff00ff) << 8;
              uVar11 = uVar10 >> 0x10 | uVar10 << 0x10;
              uVar10 = 8;
            }
            *(uint *)(lVar13 + 0x104) = uVar11;
            uVar11 = *(uint *)((long)puVar7 + (ulong)uVar10);
            uVar11 = (uVar11 & 0xff00ff00) >> 8 | (uVar11 & 0xff00ff) << 8;
            *(uint *)(lVar13 + 0x104) = uVar11 >> 0x10 | uVar11 << 0x10;
            uVar11 = *(uint *)((long)puVar7 + ((ulong)uVar10 | 4));
            uVar11 = (uVar11 & 0xff00ff00) >> 8 | (uVar11 & 0xff00ff) << 8;
            uVar10 = uVar10 + 8;
            iVar9 = 8;
            *(uint *)(lVar13 + 0x104) = uVar11 >> 0x10 | uVar11 << 0x10;
          }
          puVar7 = (uint *)((long)puVar7 + (ulong)uVar10);
          iVar6 = iVar9 + iVar6;
          uVar10 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
          *(uint *)(lVar13 + 0x104) = uVar10 >> 0x10 | uVar10 << 0x10;
          uVar10 = (puVar7[1] & 0xff00ff00) >> 8 | (puVar7[1] & 0xff00ff) << 8;
          *(uint *)(lVar13 + 0x104) = uVar10 >> 0x10 | uVar10 << 0x10;
          uVar10 = (puVar7[2] & 0xff00ff00) >> 8 | (puVar7[2] & 0xff00ff) << 8;
          *(uint *)(lVar13 + 0x104) = uVar10 >> 0x10 | uVar10 << 0x10;
          uVar10 = (puVar7[3] & 0xff00ff00) >> 8 | (puVar7[3] & 0xff00ff) << 8;
          *(uint *)(lVar13 + 0x104) = uVar10 >> 0x10 | uVar10 << 0x10;
        }
        if (((local_78._6_1_ >> 6 & 1) != 0) && (((byte)local_7c >> 5 & 1) != 0)) {
          iVar6 = iVar6 + 7;
          *(uint *)(lVar13 + 0x104) = (uint)(uVar2 >> 0x20) & 0xf;
          *(uint *)(lVar13 + 0x104) = (uint)((ulong)lVar8 >> 0x20) & 0xf;
          *(uint *)(lVar13 + 0x104) = (uint)((ulong)lVar4 >> 0x20) & 0xf;
          *(uint *)(lVar13 + 0x104) = (uint)(lVar4 + uVar5 + -1 >> 0x20) & 0xf;
          *(undefined4 *)(lVar13 + 0x104) = 0;
          *(undefined4 *)(lVar13 + 0x104) = 0;
          *(undefined4 *)(lVar13 + 0x104) = 0;
        }
        *(uint *)(lVar13 + 0x100) = (*(uint *)(lVar13 + 0x100) & 0xff) - iVar6;
        local_44 = FUN_ffff0000907b8f14(lVar13,0,local_78._4_2_);
        if (local_44 == 0) {
          lVar13 = *param_1;
          local_64 = (*(uint *)(lVar13 + 0x1c) & 0xff00) << 8 | *(int *)(lVar13 + 0x1c) << 0x18 |
                     *(uint *)(lVar13 + 0x1c) >> 8 & 0xff00 | *(uint *)(lVar13 + 0x1c) >> 0x18;
          local_68 = (*(uint *)(lVar13 + 0x20) & 0xff00) << 8 | *(int *)(lVar13 + 0x20) << 0x18 |
                     *(uint *)(lVar13 + 0x20) >> 8 & 0xff00 | *(uint *)(lVar13 + 0x20) >> 0x18;
          local_6c = (*(uint *)(lVar13 + 0x24) & 0xff00) << 8 | *(int *)(lVar13 + 0x24) << 0x18 |
                     *(uint *)(lVar13 + 0x24) >> 8 & 0xff00 | *(uint *)(lVar13 + 0x24) >> 0x18;
          local_70 = (*(uint *)(lVar13 + 0x28) & 0xff00) << 8 | *(int *)(lVar13 + 0x28) << 0x18 |
                     *(uint *)(lVar13 + 0x28) >> 8 & 0xff00 | *(uint *)(lVar13 + 0x28) >> 0x18;
          FUN_ffff00009077eb20(param_1[5],&local_70,uVar5);
          local_44 = 0;
        }
      }
    }
  }
  else {
    local_44 = 0x726e;
  }
  return local_44;
}


