
undefined4 FUN_ffff0000be0578dc(long param_1)

{
  int iVar1;
  long *plVar2;
  undefined4 uVar3;
  
  if ((((param_1 != 0) && (*(long *)(param_1 + 0x40) != 0)) &&
      (*(code **)(param_1 + 0x48) != (code *)0x0)) &&
     ((plVar2 = *(long **)(param_1 + 0x38), plVar2 != (long *)0x0 && (*plVar2 == param_1)))) {
    iVar1 = (int)plVar2[1];
    if (((iVar1 - 0x39U < 0x39) &&
        ((1L << ((ulong)(iVar1 - 0x39U) & 0x3f) & 0x100400400011001U) != 0)) ||
       ((iVar1 == 0x29a || (iVar1 == 0x2a)))) {
      if (plVar2[2] != 0) {
        (**(code **)(param_1 + 0x48))(*(undefined8 *)(param_1 + 0x50),plVar2[2]);
        plVar2 = *(long **)(param_1 + 0x38);
      }
      if (plVar2[0xf] != 0) {
        (**(code **)(param_1 + 0x48))(*(undefined8 *)(param_1 + 0x50),plVar2[0xf]);
        plVar2 = *(long **)(param_1 + 0x38);
      }
      if (plVar2[0xe] != 0) {
        (**(code **)(param_1 + 0x48))(*(undefined8 *)(param_1 + 0x50),plVar2[0xe]);
        plVar2 = *(long **)(param_1 + 0x38);
      }
      if (plVar2[0xc] != 0) {
        (**(code **)(param_1 + 0x48))(*(undefined8 *)(param_1 + 0x50),plVar2[0xc]);
        plVar2 = *(long **)(param_1 + 0x38);
      }
      (**(code **)(param_1 + 0x48))(*(undefined8 *)(param_1 + 0x50),plVar2);
      uVar3 = 0xfffffffd;
      if (iVar1 != 0x71) {
        uVar3 = 0;
      }
      *(undefined8 *)(param_1 + 0x38) = 0;
      return uVar3;
    }
  }
  return 0xfffffffe;
}


