// File offset entry: 0x91d18

long FUN_02091c18(ulong param_1)

{
  long lVar1;
  ulong uVar2;
  
  uVar2 = func_0x02091b88();
  lVar1 = (uVar2 - param_1) + 0xffffffff;
  if (param_1 <= uVar2) {
    lVar1 = uVar2 - param_1;
  }
  return lVar1;
}


