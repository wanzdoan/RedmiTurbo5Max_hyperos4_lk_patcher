// ffff0000907c1090  size=156  FUN_ffff0000907c1090

bool FUN_ffff0000907c1090(byte *param_1,uint param_2,byte *param_3,ulong param_4)

{
  byte *pbVar1;
  byte *pbVar2;
  byte *pbVar3;
  ulong uVar4;
  ulong uVar5;
  uint uVar6;
  byte *pbVar7;
  ulong uVar8;
  
  if (param_4 == 1) {
    uVar4 = 1;
    pbVar2 = param_3;
    pbVar3 = param_1;
  }
  else {
    uVar5 = param_4 & 0xfffffffffffffffe;
    uVar6 = 0;
    param_2 = 0;
    pbVar2 = param_3 + uVar5;
    pbVar3 = param_1 + uVar5;
    uVar4 = param_4 & 1;
    pbVar1 = param_1 + 1;
    pbVar7 = param_3 + 1;
    uVar8 = uVar5;
    do {
      uVar8 = uVar8 - 2;
      uVar6 = uVar6 | pbVar7[-1] ^ pbVar1[-1];
      param_2 = param_2 | *pbVar7 ^ *pbVar1;
      pbVar1 = pbVar1 + 2;
      pbVar7 = pbVar7 + 2;
    } while (uVar8 != 0);
    param_2 = param_2 | uVar6;
    if (uVar5 == param_4) goto LAB_ffff0000907c111c;
  }
  do {
    uVar4 = uVar4 - 1;
    param_2 = param_2 | *pbVar2 ^ *pbVar3;
    pbVar2 = pbVar2 + 1;
    pbVar3 = pbVar3 + 1;
  } while (uVar4 != 0);
LAB_ffff0000907c111c:
  return param_2 != 0;
}


