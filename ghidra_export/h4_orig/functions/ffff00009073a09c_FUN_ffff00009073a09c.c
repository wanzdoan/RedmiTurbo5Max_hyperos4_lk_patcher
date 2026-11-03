// ffff00009073a09c  size=468  FUN_ffff00009073a09c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073a09c(long param_1)

{
  long lVar1;
  long lVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  undefined4 uVar5;
  undefined4 uVar6;
  uint uVar7;
  long lVar8;
  ulong uVar9;
  
  _DAT_ffff00009086ded4 = *(uint *)(param_1 + 8);
  _DAT_ffff00009086ded8 = *(uint *)(param_1 + 0xc);
  if (2 < _DAT_ffff00009086ded4) {
    FUN_ffff00009077e11c(s_DBG_ERR_FLAG_MAX__d__<_dbg_err_f_ffff0000907e14a9,2);
    return;
  }
  if (_DAT_ffff00009086ded4 != 0) {
    lVar8 = 0;
    uVar9 = 0;
    do {
      lVar1 = param_1 + lVar8;
      *(undefined8 *)(lVar8 + -0xffff6f792124) = *(undefined8 *)(lVar1 + 0x10);
      uVar4 = *(undefined4 *)(lVar1 + 0x1c);
      *(undefined4 *)(lVar8 + -0xffff6f79211c) = *(undefined4 *)(lVar1 + 0x18);
      *(undefined4 *)(lVar8 + -0xffff6f792118) = uVar4;
      *(undefined8 *)(lVar8 + -0xffff6f792100) = *(undefined8 *)(lVar1 + 0x34);
      *(undefined4 *)(lVar8 + -0xffff6f7920f8) = *(undefined4 *)(lVar1 + 0x3c);
      FUN_ffff00009077eb20(lVar8 + -0xffff6f7920f0,lVar1 + 0x44,0x40);
      uVar7 = _DAT_ffff00009086ded8;
      if ((int)uVar9 == 0) {
        lVar2 = param_1 + lVar8;
        if (_DAT_ffff00009086ded8 < 3) {
          uVar4 = *(undefined4 *)(lVar2 + 0x24);
          uVar3 = *(undefined4 *)(lVar2 + 0x30);
          *(undefined4 *)(&DAT_ffff00009086deec + lVar8) = *(undefined4 *)(lVar1 + 0x20);
          *(undefined4 *)(&DAT_ffff00009086def0 + lVar8) = uVar4;
          *(undefined4 *)(&DAT_ffff00009086defc + lVar8) = uVar3;
        }
        else {
          _DAT_ffff00009086e078 = *(undefined4 *)(lVar2 + 0x24);
          _DAT_ffff00009086e07c = *(undefined4 *)(lVar2 + 0x30);
          _DAT_ffff00009086e074 = *(undefined4 *)(lVar1 + 0x20);
        }
        lVar1 = param_1 + lVar8;
        uVar4 = *(undefined4 *)(lVar1 + 0x2c);
        *(undefined4 *)(&DAT_ffff00009086def4 + lVar8) = *(undefined4 *)(lVar1 + 0x28);
        *(undefined4 *)(&DAT_ffff00009086def8 + lVar8) = uVar4;
        if (1 < uVar7) {
          *(undefined4 *)(lVar8 + -0xffff6f7920f4) = *(undefined4 *)(lVar1 + 0x40);
        }
      }
      else if ((int)uVar9 == 1) {
        lVar1 = param_1 + lVar8;
        *(undefined8 *)(lVar8 + -0xffff6f7920b0) = *(undefined8 *)(lVar1 + 0x84);
        *(undefined4 *)(&DAT_ffff00009086df58 + lVar8) = *(undefined4 *)(lVar1 + 0x8c);
        *(undefined8 *)(&DAT_ffff00009086df5c + lVar8) = *(undefined8 *)(lVar1 + 0x90);
        *(undefined4 *)(&DAT_ffff00009086df64 + lVar8) = *(undefined4 *)(lVar1 + 0x98);
        *(undefined8 *)(&DAT_ffff00009086df68 + lVar8) = *(undefined8 *)(lVar1 + 0x9c);
        uVar4 = *(undefined4 *)(lVar1 + 0xac);
        uVar5 = *(undefined4 *)(lVar1 + 0xb0);
        *(undefined4 *)(lVar8 + -0xffff6f792090) = *(undefined4 *)(lVar1 + 0xa4);
        uVar3 = *(undefined4 *)(lVar1 + 0xb4);
        uVar6 = *(undefined4 *)(lVar1 + 0xb8);
        *(undefined4 *)(lVar8 + -0xffff6f792088) = uVar4;
        *(undefined4 *)(lVar8 + -0xffff6f792084) = uVar5;
        uVar4 = *(undefined4 *)(lVar1 + 0xbc);
        uVar5 = *(undefined4 *)(lVar1 + 0xc0);
        *(undefined4 *)(&DAT_ffff00009086df80 + lVar8) = uVar3;
        *(undefined4 *)(&DAT_ffff00009086df84 + lVar8) = uVar6;
        uVar3 = *(undefined4 *)(lVar1 + 0xc4);
        uVar6 = *(undefined4 *)(lVar1 + 200);
        *(undefined4 *)(&DAT_ffff00009086df88 + lVar8) = uVar4;
        *(undefined4 *)(&DAT_ffff00009086df8c + lVar8) = uVar5;
        uVar4 = *(undefined4 *)(lVar1 + 0xcc);
        uVar5 = *(undefined4 *)(lVar1 + 0xd0);
        *(undefined4 *)(&DAT_ffff00009086df90 + lVar8) = uVar3;
        *(undefined4 *)(&DAT_ffff00009086df94 + lVar8) = uVar6;
        uVar3 = *(undefined4 *)(lVar1 + 0xd4);
        uVar6 = *(undefined4 *)(lVar1 + 0xd8);
        *(undefined4 *)(lVar8 + -0xffff6f792068) = uVar4;
        *(undefined4 *)(lVar8 + -0xffff6f792064) = uVar5;
        *(undefined4 *)(&DAT_ffff00009086dfa0 + lVar8) = uVar3;
        *(undefined4 *)(&DAT_ffff00009086dfa4 + lVar8) = uVar6;
      }
      uVar9 = uVar9 + 1;
      lVar8 = lVar8 + 0xcc;
    } while (uVar9 < _DAT_ffff00009086ded4);
  }
  return;
}


