// File offset entry: 0x5d530

int FUN_0205d430(undefined4 param_1,ulong param_2,ushort *param_3,undefined4 param_4,
                undefined8 param_5)

{
  byte bVar1;
  uint uVar2;
  ushort uVar3;
  ushort uVar4;
  int iVar5;
  uint uVar6;
  undefined8 extraout_x1;
  ulong uVar7;
  undefined1 auVar8 [16];
  
  iVar5 = func_0x0205d648(param_3,param_1,param_2,param_4);
  if (iVar5 != 0) {
    auVar8 = FUN_0205d900(0x20d0bbf);
    FUN_0208ada0(auVar8._0_8_,auVar8._8_8_,param_1,param_2,iVar5);
    return iVar5;
  }
  uVar7 = (ulong)(byte)param_3[1] << 0x10 | (ulong)*(byte *)((long)param_3 + 3) << 0x18 |
          (ulong)*param_3 |
          (ulong)((uint)(byte)param_3[3] << 0x10 | (uint)*(byte *)((long)param_3 + 7) << 0x18 |
                 (uint)param_3[2]) << 0x20;
  if (uVar7 == 0x5452415020494645) {
    uVar4 = param_3[9];
    uVar3 = param_3[8];
    bVar1 = *(byte *)((long)param_3 + 0x13);
    *(undefined1 *)(param_3 + 8) = 0;
    *(undefined1 *)((long)param_3 + 0x11) = 0;
    uVar2 = (uint)(byte)uVar4 << 0x10 | (uint)bVar1 << 0x18 | (uint)uVar3;
    *(undefined1 *)(param_3 + 9) = 0;
    *(undefined1 *)((long)param_3 + 0x13) = 0;
    uVar6 = func_0x0205cae8(0xffffffff,param_3,
                            (uint)(byte)param_3[7] << 0x10 |
                            (uint)*(byte *)((long)param_3 + 0xf) << 0x18 | (uint)param_3[6],param_5)
    ;
    if (uVar2 != ~uVar6) {
      auVar8 = FUN_0205d900(0x20d0bec);
      FUN_0208ada0(auVar8._0_8_,auVar8._8_8_,uVar2);
      return 1;
    }
    *(char *)((long)param_3 + 0x11) = (char)(uVar3 >> 8);
    *(char *)(param_3 + 8) = (char)uVar3;
    *(byte *)((long)param_3 + 0x13) = bVar1;
    *(byte *)(param_3 + 9) = (byte)uVar4;
    if (((ulong)(byte)param_3[0xd] << 0x10 | (ulong)*(byte *)((long)param_3 + 0x1b) << 0x18 |
         (ulong)param_3[0xc] |
        (ulong)((uint)(byte)param_3[0xf] << 0x10 | (uint)*(byte *)((long)param_3 + 0x1f) << 0x18 |
               (uint)CONCAT11(*(undefined1 *)((long)param_3 + 0x1d),(char)param_3[0xe])) << 0x20) ==
        param_2) {
      return 0;
    }
    FUN_0205d900(0x20c4d7c);
  }
  else {
    FUN_0205d900(0x20c5e67,extraout_x1,uVar7,0x20494645);
  }
  FUN_0208ada0();
  return 1;
}


