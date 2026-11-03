// File offset entry: 0xa4fe4

int FUN_020a4ee4(byte *param_1)

{
  int iVar1;
  int iVar2;
  byte *pbVar3;
  
  iVar1 = func_0x02091524(0x21231dc);
  iVar2 = func_0x02091524(param_1);
  if (iVar1 != iVar2) {
    return 1;
  }
  pbVar3 = (byte *)0x21231dc;
  while( true ) {
    if (iVar1 < 1) {
      return 0;
    }
    if ((uint)*pbVar3 - (uint)*param_1 != 0) break;
    pbVar3 = pbVar3 + 1;
    param_1 = param_1 + 1;
    iVar1 = iVar1 + -1;
  }
  return (uint)*pbVar3 - (uint)*param_1;
}


