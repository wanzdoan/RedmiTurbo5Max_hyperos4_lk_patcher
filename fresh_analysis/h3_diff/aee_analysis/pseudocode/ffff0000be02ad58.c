
void FUN_ffff0000be02ad58(long param_1)

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
  
  DAT_ffff0000be118194 = *(uint *)(param_1 + 8);
  DAT_ffff0000be118198 = *(uint *)(param_1 + 0xc);
  if (2 < DAT_ffff0000be118194) {
    FUN_ffff0000be05fd98(s_DBG_ERR_FLAG_MAX__d__<_dbg_err_f_ffff0000be0af9fc,2);
    return;
  }
  if (DAT_ffff0000be118194 != 0) {
    lVar8 = 0;
    uVar9 = 0;
    do {
      lVar1 = param_1 + lVar8;
      *(undefined8 *)((long)&DAT_ffff0000be11819c + lVar8) = *(undefined8 *)(lVar1 + 0x10);
      uVar4 = *(undefined4 *)(lVar1 + 0x1c);
      *(undefined4 *)((long)&DAT_ffff0000be1181a4 + lVar8) = *(undefined4 *)(lVar1 + 0x18);
      *(undefined4 *)((long)&DAT_ffff0000be1181a8 + lVar8) = uVar4;
      *(undefined8 *)((long)&DAT_ffff0000be1181c0 + lVar8) = *(undefined8 *)(lVar1 + 0x34);
      *(undefined4 *)((long)&DAT_ffff0000be1181c8 + lVar8) = *(undefined4 *)(lVar1 + 0x3c);
      FUN_ffff0000be0607b0(&DAT_ffff0000be1181d0 + lVar8,lVar1 + 0x44,0x40);
      uVar7 = DAT_ffff0000be118198;
      if ((int)uVar9 == 0) {
        lVar2 = param_1 + lVar8;
        if (DAT_ffff0000be118198 < 3) {
          uVar4 = *(undefined4 *)(lVar2 + 0x24);
          uVar3 = *(undefined4 *)(lVar2 + 0x30);
          *(undefined4 *)((long)&DAT_ffff0000be1181ac + lVar8) = *(undefined4 *)(lVar1 + 0x20);
          *(undefined4 *)((long)&DAT_ffff0000be1181b0 + lVar8) = uVar4;
          *(undefined4 *)((long)&DAT_ffff0000be1181bc + lVar8) = uVar3;
        }
        else {
          DAT_ffff0000be118338 = *(undefined4 *)(lVar2 + 0x24);
          DAT_ffff0000be11833c = *(undefined4 *)(lVar2 + 0x30);
          DAT_ffff0000be118334 = *(undefined4 *)(lVar1 + 0x20);
        }
        lVar1 = param_1 + lVar8;
        uVar4 = *(undefined4 *)(lVar1 + 0x2c);
        *(undefined4 *)((long)&DAT_ffff0000be1181b4 + lVar8) = *(undefined4 *)(lVar1 + 0x28);
        *(undefined4 *)((long)&DAT_ffff0000be1181b8 + lVar8) = uVar4;
        if (1 < uVar7) {
          *(undefined4 *)((long)&DAT_ffff0000be1181cc + lVar8) = *(undefined4 *)(lVar1 + 0x40);
        }
      }
      else if ((int)uVar9 == 1) {
        lVar1 = param_1 + lVar8;
        *(undefined8 *)((long)&DAT_ffff0000be118210 + lVar8) = *(undefined8 *)(lVar1 + 0x84);
        *(undefined4 *)((long)&DAT_ffff0000be118218 + lVar8) = *(undefined4 *)(lVar1 + 0x8c);
        *(undefined8 *)((long)&DAT_ffff0000be11821c + lVar8) = *(undefined8 *)(lVar1 + 0x90);
        *(undefined4 *)((long)&DAT_ffff0000be118224 + lVar8) = *(undefined4 *)(lVar1 + 0x98);
        *(undefined8 *)((long)&DAT_ffff0000be118228 + lVar8) = *(undefined8 *)(lVar1 + 0x9c);
        uVar4 = *(undefined4 *)(lVar1 + 0xac);
        uVar5 = *(undefined4 *)(lVar1 + 0xb0);
        *(undefined4 *)((long)&DAT_ffff0000be118230 + lVar8) = *(undefined4 *)(lVar1 + 0xa4);
        uVar3 = *(undefined4 *)(lVar1 + 0xb4);
        uVar6 = *(undefined4 *)(lVar1 + 0xb8);
        *(undefined4 *)((long)&DAT_ffff0000be118238 + lVar8) = uVar4;
        *(undefined4 *)((long)&DAT_ffff0000be11823c + lVar8) = uVar5;
        uVar4 = *(undefined4 *)(lVar1 + 0xbc);
        uVar5 = *(undefined4 *)(lVar1 + 0xc0);
        *(undefined4 *)((long)&DAT_ffff0000be118240 + lVar8) = uVar3;
        *(undefined4 *)((long)&DAT_ffff0000be118244 + lVar8) = uVar6;
        uVar3 = *(undefined4 *)(lVar1 + 0xc4);
        uVar6 = *(undefined4 *)(lVar1 + 200);
        *(undefined4 *)((long)&DAT_ffff0000be118248 + lVar8) = uVar4;
        *(undefined4 *)((long)&DAT_ffff0000be11824c + lVar8) = uVar5;
        uVar4 = *(undefined4 *)(lVar1 + 0xcc);
        uVar5 = *(undefined4 *)(lVar1 + 0xd0);
        *(undefined4 *)((long)&DAT_ffff0000be118250 + lVar8) = uVar3;
        *(undefined4 *)((long)&DAT_ffff0000be118254 + lVar8) = uVar6;
        uVar3 = *(undefined4 *)(lVar1 + 0xd4);
        uVar6 = *(undefined4 *)(lVar1 + 0xd8);
        *(undefined4 *)((long)&DAT_ffff0000be118258 + lVar8) = uVar4;
        *(undefined4 *)((long)&DAT_ffff0000be11825c + lVar8) = uVar5;
        *(undefined4 *)((long)&DAT_ffff0000be118260 + lVar8) = uVar3;
        *(undefined4 *)((long)&DAT_ffff0000be118264 + lVar8) = uVar6;
      }
      uVar9 = uVar9 + 1;
      lVar8 = lVar8 + 0xcc;
    } while (uVar9 < DAT_ffff0000be118194);
  }
  return;
}


