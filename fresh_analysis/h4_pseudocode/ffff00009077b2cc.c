
long FUN_ffff00009077b2cc(ulong param_1)

{
  code *pcVar1;
  long lVar2;
  ulong uVar3;
  
  pcVar1 = FUN_ffff000090816000;
  lVar2 = DAT_ffff000090816010;
  while( true ) {
    if (lVar2 == 0) {
      return 0;
    }
    if (((((byte)pcVar1[0x18] & 1) == 0) && (uVar3 = *(ulong *)pcVar1, uVar3 <= param_1)) &&
       (param_1 <= (lVar2 + uVar3) - 1)) break;
    lVar2 = *(long *)(pcVar1 + 0x38);
    pcVar1 = pcVar1 + 0x28;
  }
  return (param_1 - uVar3) + *(long *)(pcVar1 + 8);
}


