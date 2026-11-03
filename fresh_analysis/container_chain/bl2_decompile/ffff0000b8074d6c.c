
undefined8
FUN_ffff0000b8074d6c
          (long param_1,long param_2,long param_3,ulong param_4,undefined8 param_5,uint param_6)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  undefined8 uVar5;
  ulong uVar6;
  long lVar7;
  long *plVar8;
  long lVar9;
  int *piVar10;
  uint local_8c;
  undefined8 local_88;
  uint local_80 [16];
  
  local_80[0xc] = 0;
  local_80[0xd] = 0;
  local_80[0xe] = 0;
  local_80[0xf] = 0;
  local_80[8] = 0;
  local_80[9] = 0;
  local_80[10] = 0;
  local_80[0xb] = 0;
  local_80[4] = 0;
  local_80[5] = 0;
  local_80[6] = 0;
  local_80[7] = 0;
  local_80[0] = 0;
  local_80[1] = 0;
  local_80[2] = 0;
  local_80[3] = 0;
  if (param_2 == 0) {
    uVar5 = 0x7247;
  }
  else if ((ulong)(param_3 * 8) < param_4) {
    uVar5 = 0x7246;
  }
  else {
    local_88 = 0;
    local_8c = 0;
    thunk_FUN_ffff0000b800758c(param_2,param_3);
    FUN_ffff0000b804d070((long)&local_88 + 4,0,4);
    FUN_ffff0000b804d070(&local_88,0,4);
    FUN_ffff0000b804d070(&local_8c,0,4);
    uVar6 = thunk_FUN_ffff0000b8049ec8(param_2);
    thunk_FUN_ffff0000b8049ec8(0);
    do {
    } while ((*(uint *)(param_1 + 0x100) & 0xf8) == 0);
    uVar1 = 0x30000080;
    if (param_4 >> 0x20 == 0) {
      uVar1 = 0x30000000;
    }
    uVar2 = uVar1 & 0xf8000000 | (param_6 & 3) << 0x19 |
            uVar1 & 0x1ffffff | (param_6 >> 2 & 1) << 0xb | (uint)(uVar6 >> 0x20 != 0) << 8 |
            local_88._4_4_ & 0xc18ff67f;
    piVar10 = (int *)(param_1 + 0x2c);
    uVar1 = *piVar10 + 1U & 0xffff;
    if ((*piVar10 + 1U & 0xfffe) == 0) {
      uVar1 = 1;
    }
    uVar3 = uVar1 | (uint)local_88 & 0xffff0000 | 0x80000000;
    local_88 = CONCAT44(uVar2,uVar3);
    uVar5 = local_88;
    if (uVar6 >> 0x20 != 0) {
      local_8c = local_8c & 0xfff00000 | local_8c & 0xffff | ((uint)(uVar6 >> 0x20) & 0xf) << 0x10;
    }
    *(uint *)(param_1 + 0x104) = uVar2;
    *(uint *)(param_1 + 0x104) = uVar3;
    *(uint *)(param_1 + 0x104) = local_8c;
    *(int *)(param_1 + 0x104) = (int)param_4;
    *(int *)(param_1 + 0x104) = (int)uVar6;
    local_88._4_1_ = (char)uVar2;
    if (local_88._4_1_ < '\0') {
      iVar4 = -6;
      *(int *)(param_1 + 0x104) = (int)(param_4 >> 0x20);
    }
    else {
      iVar4 = -5;
    }
    *(uint *)(param_1 + 0x100) = iVar4 + (*(uint *)(param_1 + 0x100) & 0xff);
    local_88 = uVar5;
    iVar4 = FUN_ffff0000b8076d94(piVar10,0xffff,uVar1,5000);
    if (iVar4 == 0) {
      if (*piVar10 < 0) {
        if ((param_6 < 0xe) && ((0x3cfbU >> (ulong)(param_6 & 0x1f) & 1) != 0)) {
          uVar6 = *(ulong *)(&DAT_ffff0000b80aaa50 + (long)(int)param_6 * 8);
          FUN_ffff0000b804d070(local_80,0,0x40);
          lVar7 = (uVar6 >> 2) - 1;
          plVar8 = &DAT_ffff0000b80aa9b8;
          do {
            lVar9 = *plVar8;
            local_80[lVar7] =
                 (*(uint *)(lVar9 + param_1) & 0xff00) << 8 | *(int *)(lVar9 + param_1) << 0x18 |
                 *(uint *)(lVar9 + param_1) >> 8 & 0xff00 | *(uint *)(lVar9 + param_1) >> 0x18;
            lVar7 = lVar7 + -1;
            plVar8 = plVar8 + 1;
          } while (lVar7 != -1);
          FUN_ffff0000b804cd40(param_5,local_80,uVar6);
          uVar5 = 0;
        }
        else {
          uVar5 = 0x7250;
        }
      }
      else {
        uVar5 = 0x724e;
      }
    }
    else {
      uVar5 = 0x7275;
    }
  }
  return uVar5;
}


