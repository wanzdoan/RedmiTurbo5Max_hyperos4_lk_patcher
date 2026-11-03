// File offset entry: 0x6be54

bool FUN_0206bd54(ulong param_1,long param_2)

{
  long *plVar1;
  ulong uVar2;
  ulong uVar3;
  bool bVar4;
  ulong uVar5;
  
  uVar5 = (ulong)*(uint *)(*plRam0000000002103338 + 0x82300);
  uVar2 = param_2 + param_1;
  plVar1 = (long *)(*plRam0000000002103338 + 0x82310);
  while( true ) {
    if (uVar5 == 0) {
      return false;
    }
    if (((ulong)plVar1[-1] <= param_1) &&
       (uVar3 = *plVar1 + plVar1[-1], param_1 < uVar3 && uVar2 <= uVar3)) break;
    uVar5 = uVar5 - 1;
    plVar1 = plVar1 + 2;
  }
  if (param_2 == 0) {
    return false;
  }
  if (uVar2 < 0x100000001) {
    return false;
  }
  if ((((param_1 >> 0x20 != 0) && ((param_1 >> 0x21 != 0 || (uVar2 < 0x200000001)))) &&
      ((param_1 >> 0x22 != 0 || (uVar2 < 0x400000001)))) &&
     ((param_1 >> 0x23 != 0 || (uVar2 < 0x800000001)))) {
    bVar4 = param_1 >> 0x24 != 0;
    return (!bVar4 && 0xfffffffff < uVar2) && (bVar4 || uVar2 != 0x1000000000);
  }
  return true;
}


