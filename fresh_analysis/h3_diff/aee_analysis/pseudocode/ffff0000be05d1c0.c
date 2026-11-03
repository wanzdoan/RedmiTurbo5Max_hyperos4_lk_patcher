
long FUN_ffff0000be05d1c0(ulong param_1)

{
  ulong *puVar1;
  ulong uVar2;
  ulong uVar3;
  
  puVar1 = &DAT_ffff0000be0d5000;
  uVar2 = DAT_ffff0000be0d5010;
  while( true ) {
    if (uVar2 == 0) {
      return 0;
    }
    if ((((puVar1[3] & 1) == 0) && (uVar3 = *puVar1, uVar3 <= param_1)) &&
       (param_1 <= (uVar2 + uVar3) - 1)) break;
    uVar2 = puVar1[7];
    puVar1 = puVar1 + 5;
  }
  return (param_1 - uVar3) + puVar1[1];
}


