// File offset entry: 0x8c934

ulong FUN_0208c834(long param_1)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  long *plVar4;
  
  if (param_1 == 0) {
    uVar2 = 0x20c0273;
  }
  else {
    plVar4 = (long *)0x20ed020;
    for (uVar3 = 0; uVar3 != 0x12; uVar3 = uVar3 + 1) {
      if (((((plVar4[-1] != 0) && (iVar1 = func_0x02091544(param_1), iVar1 == 0)) ||
           ((*plVar4 != 0 && (iVar1 = func_0x02091544(param_1), iVar1 == 0)))) ||
          ((plVar4[1] != 0 && (iVar1 = func_0x02091544(param_1), iVar1 == 0)))) ||
         ((plVar4[2] != 0 && (iVar1 = func_0x02091544(param_1), iVar1 == 0)))) goto LAB_0208c8dc;
      plVar4 = plVar4 + 7;
    }
    uVar2 = 0x20c116f;
  }
  func_0x0208ada0(uVar2);
  uVar3 = 0;
LAB_0208c8dc:
  return uVar3 & 0xffffffff;
}


