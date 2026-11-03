// ffff0000907b9f3c  size=984  FUN_ffff0000907b9f3c

undefined4
FUN_ffff0000907b9f3c
          (long param_1,long param_2,uint param_3,long param_4,uint param_5,long param_6,
          uint param_7,long param_8,uint param_9)

{
  undefined2 *puVar1;
  uint uVar2;
  int iVar3;
  uint *puVar4;
  long lVar5;
  undefined4 uVar6;
  ulong uVar7;
  undefined1 *puVar8;
  undefined1 *puVar9;
  ulong uVar10;
  uint *puVar11;
  ulong uVar12;
  ulong uVar13;
  uint uVar14;
  undefined8 local_f0;
  undefined8 local_e8;
  long local_e0;
  ulong local_d8;
  ulong local_d0;
  ulong local_c8;
  uint local_c0 [24];
  
  if (param_2 == 0) {
    return 0x7247;
  }
  if (param_4 == 0) {
    return 0x7247;
  }
  local_d0 = 0;
  local_c8 = 0;
  local_e0 = 0;
  local_d8 = 0;
  local_f0 = 0;
  local_e8 = 0;
  FUN_ffff00009077eb88(&local_f0,0,0x30);
  local_d8 = (ulong)param_3;
  local_d0 = (ulong)param_7;
  local_c8 = (ulong)param_5;
  local_f0 = 1;
  if (param_6 != 0) {
    if (param_7 == 0) {
      return 0x7247;
    }
    local_c0[0x14] = 0;
    local_c0[0x15] = 0;
    local_c0[0x16] = 0;
    local_c0[0x17] = 0;
    local_c0[0x10] = 0;
    local_c0[0x11] = 0;
    local_c0[0x12] = 0;
    local_c0[0x13] = 0;
    local_c0[0xc] = 0;
    local_c0[0xd] = 0;
    local_c0[0xe] = 0;
    local_c0[0xf] = 0;
    local_c0[8] = 0;
    local_c0[9] = 0;
    local_c0[10] = 0;
    local_c0[0xb] = 0;
    local_c0[4] = 0;
    local_c0[5] = 0;
    local_c0[6] = 0;
    local_c0[7] = 0;
    local_c0[0] = 0;
    local_c0[1] = 0;
    local_c0[2] = 0;
    local_c0[3] = 0;
    if (0x60 < param_7) {
      return 0x724c;
    }
    uVar2 = param_7 >> 2;
    if ((param_7 & 3) != 0) {
      uVar2 = uVar2 + 1;
    }
    uVar12 = (ulong)uVar2;
    if (0x18 < uVar2) {
      return 0x724c;
    }
    uVar14 = uVar2 * 4;
    uVar10 = local_d0;
    do {
      uVar14 = uVar14 - 1;
      uVar10 = uVar10 - 1;
      *(undefined1 *)((long)local_c0 + (ulong)uVar14) =
           *(undefined1 *)(param_6 + (uVar10 & 0xffffffff));
    } while (uVar10 != 0);
    if (uVar2 != 0) {
      puVar4 = (uint *)(param_1 + 0x140);
      do {
        uVar12 = uVar12 - 1;
        uVar2 = (local_c0[uVar12 & 0xffffffff] & 0xff00ff00) >> 8 |
                (local_c0[uVar12 & 0xffffffff] & 0xff00ff) << 8;
        *puVar4 = uVar2 >> 0x10 | uVar2 << 0x10;
        puVar4 = puVar4 + 1;
      } while (uVar12 != 0);
    }
  }
  local_e8 = CONCAT44(1,(uint)(param_6 == 0));
  local_c0[0xc] = 0;
  local_c0[0xd] = 0;
  local_c0[0xe] = 0;
  local_c0[0xf] = 0;
  local_c0[8] = 0;
  local_c0[9] = 0;
  local_c0[10] = 0;
  local_c0[0xb] = 0;
  local_c0[4] = 0;
  local_c0[5] = 0;
  local_c0[6] = 0;
  local_c0[7] = 0;
  local_c0[0] = 0;
  local_c0[1] = 0;
  local_c0[2] = 0;
  local_c0[3] = 0;
  if (param_3 < 0x41) {
    uVar2 = param_3 >> 2;
    if ((param_3 & 3) != 0) {
      uVar2 = uVar2 + 1;
    }
    uVar12 = (ulong)uVar2;
    if (uVar2 < 0x11) {
      if (param_3 != 0) {
        uVar14 = uVar2 * 4;
        uVar10 = local_d8;
        do {
          uVar14 = uVar14 - 1;
          uVar10 = uVar10 - 1;
          *(undefined1 *)((long)local_c0 + (ulong)uVar14) =
               *(undefined1 *)(param_2 + (uVar10 & 0xffffffff));
        } while (uVar10 != 0);
      }
      if (uVar2 != 0) {
        puVar4 = (uint *)(param_1 + 0x100);
        do {
          uVar12 = uVar12 - 1;
          uVar2 = (local_c0[uVar12 & 0xffffffff] & 0xff00ff00) >> 8 |
                  (local_c0[uVar12 & 0xffffffff] & 0xff00ff) << 8;
          *puVar4 = uVar2 >> 0x10 | uVar2 << 0x10;
          puVar4 = puVar4 + 1;
        } while (uVar12 != 0);
      }
      local_c0[8] = 0;
      local_c0[9] = 0;
      local_c0[10] = 0;
      local_c0[0xb] = 0;
      local_c0[4] = 0;
      local_c0[5] = 0;
      local_c0[6] = 0;
      local_c0[7] = 0;
      local_c0[0] = 0;
      local_c0[1] = 0;
      local_c0[2] = 0;
      local_c0[3] = 0;
      if (param_5 < 0x31) {
        uVar2 = param_5 >> 2;
        if ((param_5 & 3) != 0) {
          uVar2 = uVar2 + 1;
        }
        uVar12 = (ulong)uVar2;
        if (uVar2 < 0xd) {
          if (param_5 != 0) {
            uVar14 = uVar2 * 4;
            uVar10 = local_c8;
            do {
              uVar14 = uVar14 - 1;
              uVar10 = uVar10 - 1;
              *(undefined1 *)((long)local_c0 + (ulong)uVar14) =
                   *(undefined1 *)(param_4 + (uVar10 & 0xffffffff));
            } while (uVar10 != 0);
          }
          if (uVar2 != 0) {
            puVar4 = (uint *)(param_1 + 0x160);
            do {
              uVar12 = uVar12 - 1;
              uVar2 = (local_c0[uVar12 & 0xffffffff] & 0xff00ff00) >> 8 |
                      (local_c0[uVar12 & 0xffffffff] & 0xff00ff) << 8;
              *puVar4 = uVar2 >> 0x10 | uVar2 << 0x10;
              puVar4 = puVar4 + 1;
            } while (uVar12 != 0);
          }
          *(uint *)(param_1 + 0x19c) =
               (uint)(param_6 == 0) << 4 | 0x82 | (param_3 & 0xff) << 8 | (param_7 & 0xff) << 0x10 |
               param_5 << 0x18 | 1;
          local_e0 = param_1;
          iVar3 = FUN_ffff0000907bb6f0(param_1 + 0x19c,1,1,5000);
          if (iVar3 != 0) {
            return 0x7262;
          }
          uVar2 = *(uint *)(param_1 + 0x198);
          if ((uVar2 & 0x3f) != 0) {
            if ((uVar2 & 1) != 0) {
              lVar5 = 0;
code_r0xffff0000907ba2b0:
              return (&DAT_ffff00009081345c)[lVar5 * 2];
            }
            if ((uVar2 >> 1 & 1) != 0) {
              lVar5 = 1;
              goto code_r0xffff0000907ba2b0;
            }
            if ((uVar2 >> 2 & 1) != 0) {
              lVar5 = 2;
              goto code_r0xffff0000907ba2b0;
            }
            if ((uVar2 >> 3 & 1) != 0) {
              lVar5 = 3;
              goto code_r0xffff0000907ba2b0;
            }
            if ((uVar2 >> 4 & 1) != 0) {
              lVar5 = 4;
              goto code_r0xffff0000907ba2b0;
            }
          }
          uVar6 = 0x724d;
          if ((uVar2 & 0x9f000000) == 0x80000000 || local_f0._4_4_ == 0) {
            uVar6 = 0;
          }
          if (local_f0._4_4_ != 0) {
            return uVar6;
          }
          uVar12 = (ulong)param_9;
          local_c0[0xc] = 0;
          local_c0[0xd] = 0;
          local_c0[0xe] = 0;
          local_c0[0xf] = 0;
          local_c0[10] = 0;
          local_c0[0xb] = 0;
          local_c0[6] = 0;
          local_c0[7] = 0;
          local_c0[8] = 0;
          local_c0[9] = 0;
          local_c0[2] = 0;
          local_c0[3] = 0;
          local_c0[4] = 0;
          local_c0[5] = 0;
          local_c0[0] = 0;
          local_c0[1] = 0;
          if (((param_9 & 3) == 0) && (param_9 < 0x44)) {
            if (3 < param_9) {
              uVar2 = param_9 >> 2;
              if (uVar2 < 2) {
                uVar2 = 1;
              }
              uVar10 = (ulong)uVar2;
              puVar4 = (uint *)(param_1 + 0x1fc);
              puVar11 = local_c0;
              do {
                uVar10 = uVar10 - 1;
                *puVar11 = (*puVar4 & 0xff00) << 8 | *puVar4 << 0x18 | *puVar4 >> 8 & 0xff00 |
                           *puVar4 >> 0x18;
                puVar4 = puVar4 + -1;
                puVar11 = puVar11 + 1;
              } while (uVar10 != 0);
            }
            if (param_9 != 0) {
              if (param_9 == 1) {
                uVar7 = 0;
              }
              else {
                uVar7 = uVar12 & 0xfffffffe;
                uVar10 = (ulong)local_c0 | 1;
                lVar5 = param_8 + 1;
                uVar13 = uVar7;
                do {
                  puVar1 = (undefined2 *)(uVar10 - 1);
                  uVar10 = uVar10 + 2;
                  uVar13 = uVar13 - 2;
                  *(undefined2 *)(lVar5 + -1) = *puVar1;
                  lVar5 = lVar5 + 2;
                } while (uVar13 != 0);
                if (uVar7 == uVar12) {
                  return 0;
                }
              }
              lVar5 = uVar12 - uVar7;
              puVar8 = (undefined1 *)((long)local_c0 + uVar7);
              puVar9 = (undefined1 *)(param_8 + uVar7);
              do {
                lVar5 = lVar5 + -1;
                *puVar9 = *puVar8;
                puVar8 = puVar8 + 1;
                puVar9 = puVar9 + 1;
              } while (lVar5 != 0);
            }
            return 0;
          }
        }
      }
    }
  }
  return 0x724c;
}


