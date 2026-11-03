
int FUN_ffff0000be09f48c
              (long param_1,long param_2,undefined8 param_3,long param_4,long param_5,
              undefined8 param_6)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  byte bVar4;
  byte bVar5;
  uint uVar6;
  byte bVar7;
  byte bVar8;
  int iVar9;
  int iVar10;
  ulong uVar11;
  byte bVar12;
  ulong uVar13;
  long lVar14;
  long lVar15;
  byte *pbVar16;
  ulong uVar17;
  ulong uVar18;
  uint uVar19;
  long *plVar20;
  byte bVar21;
  ulong uVar22;
  long lVar23;
  long lVar24;
  ulong uVar25;
  ulong uVar26;
  ulong local_1f0;
  undefined8 local_1d8;
  byte local_1d0 [8];
  long local_1c8 [32];
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 local_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  
  if ((*(long *)(param_1 + 0x60) == 0) || (*(long *)(param_1 + 0x78) == 0)) {
    return -0x4f80;
  }
  iVar9 = FUN_ffff0000be09c9e4(param_3,1);
  if (iVar9 < 0) {
    return -0x4c80;
  }
  lVar24 = param_1 + 0x98;
  iVar9 = FUN_ffff0000be09c8f4(param_3,lVar24);
  if (-1 < iVar9) {
    return -0x4c80;
  }
  iVar9 = FUN_ffff0000be09fd60(param_1,param_4);
  if (iVar9 != 0) {
    return iVar9;
  }
  if (*(long *)(param_1 + 0x60) == 0) {
    return -0x4f80;
  }
  if (*(long *)(param_1 + 0x78) == 0) {
    return -0x4f80;
  }
  iVar9 = FUN_ffff0000be09c8f4(param_4 + 0x18,param_1 + 0x68);
  local_1d8 = lVar24;
  if (iVar9 == 0) {
    iVar9 = FUN_ffff0000be09c8f4(param_4,param_1 + 0x50);
    uVar19 = (uint)(iVar9 == 0);
  }
  else {
    uVar19 = 0;
  }
  uVar13 = *(ulong *)(param_1 + 0xb8);
  iVar9 = 4;
  if (0x17f < uVar13) {
    iVar9 = 5;
  }
  uVar2 = iVar9 + uVar19;
  if (uVar13 <= uVar2) {
    uVar2 = 2;
  }
  uVar26 = (ulong)uVar2;
  uVar13 = (uVar13 + uVar26) - 1;
  local_1f0 = 0;
  if (uVar26 != 0) {
    local_1f0 = uVar13 / uVar26;
  }
  uVar6 = 1 << (ulong)(uVar2 - 1 & 0x1f);
  if ((uVar19 == 0) || (lVar24 = *(long *)(param_1 + 0xe8), lVar24 == 0)) {
    lVar24 = FUN_ffff0000be05f250(uVar6,0x48);
    if (lVar24 == 0) {
      iVar9 = -0x4d80;
      goto LAB_ffff0000be09f948;
    }
    uVar25 = 0;
    do {
      lVar23 = lVar24 + (uVar25 & 0xff) * 0x48;
      FUN_ffff0000be09bcf0(lVar23);
      FUN_ffff0000be09bcf0(lVar23 + 0x18);
      FUN_ffff0000be09bcf0(lVar23 + 0x30);
      uVar1 = (int)uVar25 + 1;
      uVar25 = (ulong)uVar1;
    } while ((uVar1 & 0xff) < uVar6);
    iVar9 = FUN_ffff0000be09bf10(lVar24,param_4);
    if (((iVar9 == 0) && (iVar9 = FUN_ffff0000be09bf10(lVar24 + 0x18,param_4 + 0x18), iVar9 == 0))
       && (iVar9 = FUN_ffff0000be09bf10(lVar24 + 0x30,param_4 + 0x30), iVar9 == 0)) {
      uVar25 = local_1f0 * (uVar2 - 1);
      if (uVar25 != 0) {
        uVar22 = 0;
        do {
          uVar11 = 0;
          if (local_1f0 != 0) {
            uVar11 = uVar22 / local_1f0;
          }
          uVar2 = 1 << (ulong)((uint)uVar11 & 0x1f) & 0xff;
          uVar17 = (ulong)uVar2;
          lVar23 = lVar24 + uVar17 * 0x48;
          if (uVar22 == uVar11 * local_1f0) {
            uVar11 = (ulong)(uVar2 >> 1);
            iVar9 = FUN_ffff0000be09bf10(lVar23,lVar24 + uVar11 * 0x48);
            if (((iVar9 != 0) ||
                (iVar9 = FUN_ffff0000be09bf10
                                   (lVar24 + uVar17 * 0x48 + 0x18,lVar24 + uVar11 * 0x48 + 0x18),
                iVar9 != 0)) ||
               (iVar9 = FUN_ffff0000be09bf10
                                  (lVar24 + uVar17 * 0x48 + 0x30,lVar24 + uVar11 * 0x48 + 0x30),
               iVar9 != 0)) goto LAB_ffff0000be09f8ec;
          }
          iVar9 = FUN_ffff0000be0a09b8(param_1,lVar23);
          if (iVar9 != 0) goto LAB_ffff0000be09f8ec;
          uVar22 = uVar22 + 1;
        } while (uVar22 != uVar25);
      }
      local_1c8[0x1c] = 0;
      local_1c8[0x1d] = 0;
      local_1c8[0x1a] = 0;
      local_1c8[0x1b] = 0;
      uVar2 = uVar6 & 0xff;
      uVar25 = (ulong)uVar2;
      local_1c8[0x18] = 0;
      local_1c8[0x19] = 0;
      local_1c8[0x16] = 0;
      local_1c8[0x17] = 0;
      local_1c8[0x14] = 0;
      local_1c8[0x15] = 0;
      local_1c8[0x12] = 0;
      local_1c8[0x13] = 0;
      local_1c8[0x10] = 0;
      local_1c8[0x11] = 0;
      local_1c8[0xe] = 0;
      local_1c8[0xf] = 0;
      local_1c8[0xc] = 0;
      local_1c8[0xd] = 0;
      local_1c8[10] = 0;
      local_1c8[0xb] = 0;
      local_1c8[8] = 0;
      local_1c8[9] = 0;
      local_1c8[6] = 0;
      local_1c8[7] = 0;
      local_1c8[4] = 0;
      local_1c8[5] = 0;
      local_1c8[2] = 0;
      local_1c8[3] = 0;
      local_1c8[0] = 0;
      local_1c8[1] = 0;
      local_1d0[0] = 0;
      local_1d0[1] = 0;
      local_1d0[2] = 0;
      local_1d0[3] = 0;
      local_1d0[4] = 0;
      local_1d0[5] = 0;
      local_1d0[6] = 0;
      local_1d0[7] = 0;
      if (uVar2 < 2) {
        iVar9 = FUN_ffff0000be0a0858(param_1,0);
        if (iVar9 == 0) {
          uVar22 = 0;
          goto LAB_ffff0000be09fc1c;
        }
      }
      else {
        uVar22 = 1;
        lVar23 = 0;
        do {
          lVar15 = uVar22 * 0x48;
          uVar1 = ((uint)uVar22 & 0x7f) << 1;
          uVar22 = (ulong)uVar1;
          local_1c8[lVar23 + -1] = lVar24 + lVar15;
          lVar23 = lVar23 + 1;
        } while (uVar1 < uVar2);
        iVar9 = FUN_ffff0000be0a1060(param_1,local_1d0);
        if (iVar9 == 0) {
          uVar22 = 1;
          do {
            lVar23 = (uVar22 & 0xff) + (uVar22 & 0xff) * 8;
            lVar14 = lVar23 * 8;
            lVar23 = lVar23 * 0x10;
            lVar15 = lVar14;
            while (lVar15 + -0x48 != -0x48) {
              lVar3 = lVar24 + -0x48 + lVar23;
              lVar23 = lVar23 + -0x48;
              iVar9 = FUN_ffff0000be0a0218(param_1,lVar3,lVar24 + -0x48 + lVar15,lVar24 + lVar14);
              lVar15 = lVar15 + -0x48;
              if (iVar9 != 0) goto LAB_ffff0000be09f8ec;
            }
            uVar1 = (int)uVar22 << 1;
            uVar22 = (ulong)uVar1;
          } while ((uVar1 & 0xfe) < (uVar6 & 0xff));
          if (uVar2 < 3) {
            uVar22 = 0;
            uVar17 = 1;
          }
          else {
            plVar20 = local_1c8;
            uVar18 = uVar25 - 1;
            uVar22 = uVar18 & 0xfffffffffffffffe;
            uVar17 = uVar18 | 1;
            uVar11 = uVar22;
            lVar23 = lVar24;
            do {
              uVar11 = uVar11 - 2;
              plVar20[-1] = lVar23 + 0x48;
              *plVar20 = lVar23 + 0x90;
              plVar20 = plVar20 + 2;
              lVar23 = lVar23 + 0x90;
            } while (uVar11 != 0);
            if (uVar18 == uVar22) goto LAB_ffff0000be09fc1c;
          }
          uVar11 = uVar22;
          do {
            uVar22 = uVar17;
            local_1c8[uVar11 - 1] = lVar24 + 0x48 + uVar11 * 0x48;
            uVar11 = uVar22;
            uVar17 = uVar22 + 1;
          } while (uVar25 != uVar22 + 1);
LAB_ffff0000be09fc1c:
          iVar9 = FUN_ffff0000be0a1060(param_1,local_1d0,uVar22);
          if (iVar9 == 0) {
            if (uVar19 != 0) {
              *(long *)(param_1 + 0xe8) = lVar24;
              *(ulong *)(param_1 + 0xf0) = uVar25;
            }
            goto LAB_ffff0000be09f5d0;
          }
        }
      }
    }
  }
  else {
LAB_ffff0000be09f5d0:
    FUN_ffff0000be060ae0(local_1d0,0,0x106);
    local_a8 = 0;
    uStack_a0 = 0;
    local_b8 = 0;
    local_b0 = 0;
    local_c8 = 0;
    uStack_c0 = 0;
    FUN_ffff0000be09bcf0(&local_b0);
    FUN_ffff0000be09bcf0(&local_c8);
    iVar9 = FUN_ffff0000be09c29c(local_1d8,0);
    if (iVar9 == 1) {
      iVar10 = FUN_ffff0000be09c29c(param_3,0);
      iVar9 = FUN_ffff0000be09bf10(&local_b0,param_3);
      if (((iVar9 == 0) && (iVar9 = FUN_ffff0000be09d008(&local_c8,local_1d8,param_3), iVar9 == 0))
         && ((iVar9 = FUN_ffff0000be09c06c(&local_b0,&local_c8,iVar10 == 0), iVar9 == 0 &&
             (FUN_ffff0000be060ae0(local_1d0,0,local_1f0 + 1), uVar26 <= uVar13)))) {
        uVar25 = 0;
        do {
          uVar22 = 0;
          do {
            iVar9 = FUN_ffff0000be09c29c(&local_b0,uVar25 + uVar22 * local_1f0);
            uVar19 = (uint)uVar22;
            uVar22 = uVar22 + 1;
            *(byte *)((long)local_1c8 + (uVar25 - 8)) =
                 *(byte *)((long)local_1c8 + (uVar25 - 8)) | (byte)(iVar9 << (ulong)(uVar19 & 0x1f))
            ;
          } while (uVar26 != uVar22);
          uVar25 = uVar25 + 1;
        } while (uVar25 != local_1f0);
        bVar12 = 0;
        iVar9 = 0;
        pbVar16 = (byte *)((ulong)local_1d0 | 1);
        uVar25 = local_1f0;
        bVar21 = local_1d0[0];
        do {
          bVar5 = *pbVar16;
          uVar25 = uVar25 - 1;
          bVar4 = bVar5 ^ bVar12;
          bVar7 = ((bVar4 ^ 0xff) & 1) * bVar21;
          bVar8 = bVar21 | (bVar5 ^ bVar12 ^ 0xff) << 7;
          bVar21 = bVar7 ^ bVar4;
          pbVar16[-1] = bVar8;
          bVar12 = bVar7 & bVar4 | bVar5 & bVar12;
          *pbVar16 = bVar21;
          pbVar16 = pbVar16 + 1;
        } while (uVar25 != 0);
      }
      FUN_ffff0000be09bd08(&local_c8);
      FUN_ffff0000be09bd08(&local_b0);
      if (iVar9 == 0) {
        local_78 = 0;
        uStack_70 = 0;
        local_88 = 0;
        uStack_80 = 0;
        local_98 = 0;
        uStack_90 = 0;
        local_a8 = 0;
        uStack_a0 = 0;
        local_b0 = 0;
        FUN_ffff0000be09bcf0(&local_b0);
        FUN_ffff0000be09bcf0(&local_98);
        FUN_ffff0000be09bcf0(&uStack_80);
        iVar9 = FUN_ffff0000be0a1638
                          (param_1,param_2,lVar24,uVar6,
                           *(undefined1 *)((long)local_1c8 + (local_1f0 - 8)));
        if ((iVar9 == 0) && (iVar9 = FUN_ffff0000be09c1d8(param_2 + 0x30,1), iVar9 == 0)) {
          if (param_5 == 0) {
            if (uVar26 <= uVar13) goto LAB_ffff0000be09fc58;
            iVar9 = 0;
          }
          else {
            iVar9 = FUN_ffff0000be0a1460(param_1,param_2,param_5,param_6);
            if ((uVar26 <= uVar13) && (iVar9 == 0)) {
LAB_ffff0000be09fc58:
              while (iVar9 = FUN_ffff0000be0a09b8(param_1,param_2,param_2), iVar9 == 0) {
                uVar13 = local_1f0 - 1;
                iVar9 = FUN_ffff0000be0a1638
                                  (param_1,&local_b0,lVar24,uVar6,
                                   *(undefined1 *)((long)&local_1d8 + local_1f0 + 7));
                if (((iVar9 != 0) ||
                    (iVar9 = FUN_ffff0000be0a0218(param_1,param_2,param_2,&local_b0), iVar9 != 0))
                   || (local_1f0 = uVar13, uVar13 == 0)) break;
              }
            }
          }
        }
        FUN_ffff0000be09bd08(&local_b0);
        FUN_ffff0000be09bd08(&local_98);
        FUN_ffff0000be09bd08(&uStack_80);
        if (iVar9 == 0) {
          local_a8 = 0;
          uStack_a0 = 0;
          local_b0 = 0;
          FUN_ffff0000be09bcf0(&local_b0);
          lVar23 = param_2 + 0x18;
          iVar9 = FUN_ffff0000be09d008(&local_b0,param_1 + 8,lVar23);
          if (iVar9 == 0) {
            iVar9 = FUN_ffff0000be09c9e4(lVar23,0);
            iVar9 = FUN_ffff0000be09c06c(lVar23,&local_b0,iVar10 == 0 && iVar9 != 0);
            FUN_ffff0000be09bd08(&local_b0);
            if ((iVar9 == 0) &&
               ((param_5 == 0 ||
                (iVar9 = FUN_ffff0000be0a1460(param_1,param_2,param_5,param_6), iVar9 == 0)))) {
              iVar9 = FUN_ffff0000be0a0858(param_1,param_2);
            }
          }
          else {
            FUN_ffff0000be09bd08(&local_b0);
          }
        }
      }
    }
    else {
      iVar9 = -0x4f80;
    }
  }
LAB_ffff0000be09f8ec:
  if ((lVar24 != 0) && (lVar24 != *(long *)(param_1 + 0xe8))) {
    uVar19 = uVar6 & 0xff;
    if ((uVar6 & 0xfe) == 0) {
      uVar19 = 1;
    }
    lVar23 = 0;
    do {
      lVar15 = lVar24 + lVar23;
      FUN_ffff0000be09bd08(lVar15);
      FUN_ffff0000be09bd08(lVar15 + 0x18);
      FUN_ffff0000be09bd08(lVar15 + 0x30);
      lVar23 = lVar23 + 0x48;
    } while ((ulong)uVar19 * 0x48 - lVar23 != 0);
    thunk_FUN_ffff0000be09937c(lVar24);
  }
  if (iVar9 == 0) {
    return 0;
  }
LAB_ffff0000be09f948:
  if (param_2 != 0) {
    FUN_ffff0000be09bd08(param_2);
    FUN_ffff0000be09bd08(param_2 + 0x18);
    FUN_ffff0000be09bd08(param_2 + 0x30);
    return iVar9;
  }
  return iVar9;
}


