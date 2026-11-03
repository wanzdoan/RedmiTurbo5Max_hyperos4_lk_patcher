// ffff0000907782f4  size=148  FUN_ffff0000907782f4

undefined8 FUN_ffff0000907782f4(long param_1)

{
  undefined8 uVar1;
  long *plVar2;
  code *pcVar3;
  
  if ((((param_1 == 0) || (*(long *)(param_1 + 0x40) == 0)) ||
      (pcVar3 = *(code **)(param_1 + 0x48), pcVar3 == (code *)0x0)) ||
     (((plVar2 = *(long **)(param_1 + 0x38), plVar2 == (long *)0x0 || (*plVar2 != param_1)) ||
      (0x1f < (int)plVar2[1] - 0x3f34U)))) {
    uVar1 = 0xfffffffe;
  }
  else {
    if (plVar2[9] != 0) {
      (*pcVar3)(*(undefined8 *)(param_1 + 0x50),plVar2[9]);
      pcVar3 = *(code **)(param_1 + 0x48);
      plVar2 = *(long **)(param_1 + 0x38);
    }
    (*pcVar3)(*(undefined8 *)(param_1 + 0x50),plVar2);
    uVar1 = 0;
    *(undefined8 *)(param_1 + 0x38) = 0;
  }
  return uVar1;
}


