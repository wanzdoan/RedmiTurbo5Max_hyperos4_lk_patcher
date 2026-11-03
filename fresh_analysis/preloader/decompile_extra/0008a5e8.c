// File offset entry: 0x8a5e8

long FUN_0208a4e8(ulong param_1)

{
  long lVar1;
  ulong uVar3;
  long *plVar2;
  
  plVar2 = (long *)0x20eccb8;
  while( true ) {
    lVar1 = *plVar2;
    if (lVar1 == 0x20eccb0) {
      return -1;
    }
    uVar3 = *(ulong *)(lVar1 + 0x38);
    if ((uVar3 <= param_1) && (param_1 < uVar3 + (*(ulong *)(lVar1 + 0x28) >> 0xc) * 0x18)) break;
    plVar2 = (long *)(lVar1 + 8);
  }
  return *(long *)(lVar1 + 0x20) + ((param_1 - uVar3) / 0x18) * 0x1000;
}


