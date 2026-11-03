
void FUN_ffff0000b8021e58(long param_1)

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
  
  DAT_ffff0000b811b960 = *(uint *)(param_1 + 8);
  DAT_ffff0000b811b964 = *(uint *)(param_1 + 0xc);
  if (2 < DAT_ffff0000b811b960) {
    FUN_ffff0000b804c33c(s_DBG_ERR_FLAG_MAX__d__<_dbg_err_f_ffff0000b808ed71,2);
    return;
  }
  if (DAT_ffff0000b811b960 != 0) {
    lVar8 = 0;
    uVar9 = 0;
    do {
      lVar1 = param_1 + lVar8;
      *(undefined8 *)((long)&DAT_ffff0000b811b968 + lVar8) = *(undefined8 *)(lVar1 + 0x10);
      uVar4 = *(undefined4 *)(lVar1 + 0x1c);
      *(undefined4 *)((long)&DAT_ffff0000b811b970 + lVar8) = *(undefined4 *)(lVar1 + 0x18);
      *(undefined4 *)((long)&DAT_ffff0000b811b974 + lVar8) = uVar4;
      *(undefined8 *)((long)&DAT_ffff0000b811b98c + lVar8) = *(undefined8 *)(lVar1 + 0x34);
      *(undefined4 *)((long)&DAT_ffff0000b811b994 + lVar8) = *(undefined4 *)(lVar1 + 0x3c);
      FUN_ffff0000b804cd40(&DAT_ffff0000b811b99c + lVar8,lVar1 + 0x44,0x40);
      uVar7 = DAT_ffff0000b811b964;
      if ((int)uVar9 == 0) {
        lVar2 = param_1 + lVar8;
        if (DAT_ffff0000b811b964 < 3) {
          uVar4 = *(undefined4 *)(lVar2 + 0x24);
          uVar3 = *(undefined4 *)(lVar2 + 0x30);
          *(undefined4 *)((long)&DAT_ffff0000b811b978 + lVar8) = *(undefined4 *)(lVar1 + 0x20);
          *(undefined4 *)((long)&DAT_ffff0000b811b97c + lVar8) = uVar4;
          *(undefined4 *)((long)&DAT_ffff0000b811b988 + lVar8) = uVar3;
        }
        else {
          DAT_ffff0000b811bb04 = *(undefined4 *)(lVar2 + 0x24);
          DAT_ffff0000b811bb08 = *(undefined4 *)(lVar2 + 0x30);
          DAT_ffff0000b811bb00 = *(undefined4 *)(lVar1 + 0x20);
        }
        lVar1 = param_1 + lVar8;
        uVar4 = *(undefined4 *)(lVar1 + 0x2c);
        *(undefined4 *)((long)&DAT_ffff0000b811b980 + lVar8) = *(undefined4 *)(lVar1 + 0x28);
        *(undefined4 *)((long)&DAT_ffff0000b811b984 + lVar8) = uVar4;
        if (1 < uVar7) {
          *(undefined4 *)((long)&DAT_ffff0000b811b998 + lVar8) = *(undefined4 *)(lVar1 + 0x40);
        }
      }
      else if ((int)uVar9 == 1) {
        lVar1 = param_1 + lVar8;
        *(undefined8 *)((long)&DAT_ffff0000b811b9dc + lVar8) = *(undefined8 *)(lVar1 + 0x84);
        *(undefined4 *)((long)&DAT_ffff0000b811b9e4 + lVar8) = *(undefined4 *)(lVar1 + 0x8c);
        *(undefined8 *)((long)&DAT_ffff0000b811b9e8 + lVar8) = *(undefined8 *)(lVar1 + 0x90);
        *(undefined4 *)((long)&DAT_ffff0000b811b9f0 + lVar8) = *(undefined4 *)(lVar1 + 0x98);
        *(undefined8 *)((long)&DAT_ffff0000b811b9f4 + lVar8) = *(undefined8 *)(lVar1 + 0x9c);
        uVar4 = *(undefined4 *)(lVar1 + 0xac);
        uVar5 = *(undefined4 *)(lVar1 + 0xb0);
        *(undefined4 *)((long)&DAT_ffff0000b811b9fc + lVar8) = *(undefined4 *)(lVar1 + 0xa4);
        uVar3 = *(undefined4 *)(lVar1 + 0xb4);
        uVar6 = *(undefined4 *)(lVar1 + 0xb8);
        *(undefined4 *)((long)&DAT_ffff0000b811ba04 + lVar8) = uVar4;
        *(undefined4 *)((long)&DAT_ffff0000b811ba08 + lVar8) = uVar5;
        uVar4 = *(undefined4 *)(lVar1 + 0xbc);
        uVar5 = *(undefined4 *)(lVar1 + 0xc0);
        *(undefined4 *)((long)&DAT_ffff0000b811ba0c + lVar8) = uVar3;
        *(undefined4 *)((long)&DAT_ffff0000b811ba10 + lVar8) = uVar6;
        uVar3 = *(undefined4 *)(lVar1 + 0xc4);
        uVar6 = *(undefined4 *)(lVar1 + 200);
        *(undefined4 *)((long)&DAT_ffff0000b811ba14 + lVar8) = uVar4;
        *(undefined4 *)((long)&DAT_ffff0000b811ba18 + lVar8) = uVar5;
        uVar4 = *(undefined4 *)(lVar1 + 0xcc);
        uVar5 = *(undefined4 *)(lVar1 + 0xd0);
        *(undefined4 *)((long)&DAT_ffff0000b811ba1c + lVar8) = uVar3;
        *(undefined4 *)((long)&DAT_ffff0000b811ba20 + lVar8) = uVar6;
        uVar3 = *(undefined4 *)(lVar1 + 0xd4);
        uVar6 = *(undefined4 *)(lVar1 + 0xd8);
        *(undefined4 *)((long)&DAT_ffff0000b811ba24 + lVar8) = uVar4;
        *(undefined4 *)((long)&DAT_ffff0000b811ba28 + lVar8) = uVar5;
        *(undefined4 *)((long)&DAT_ffff0000b811ba2c + lVar8) = uVar3;
        *(undefined4 *)((long)&DAT_ffff0000b811ba30 + lVar8) = uVar6;
      }
      uVar9 = uVar9 + 1;
      lVar8 = lVar8 + 0xcc;
    } while (uVar9 < DAT_ffff0000b811b960);
  }
  return;
}


