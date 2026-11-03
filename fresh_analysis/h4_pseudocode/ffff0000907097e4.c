
undefined4 FUN_ffff0000907097e4(undefined8 param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined *puVar3;
  undefined8 *puVar4;
  undefined4 uVar5;
  
  puVar4 = (undefined8 *)FUN_ffff00009077d514(0x18);
  if (puVar4 == (undefined8 *)0x0) {
    uVar5 = 0xfffffffb;
  }
  else {
    *puVar4 = puVar4;
    puVar4[1] = puVar4;
    puVar4[2] = param_1;
    puVar3 = PTR_LOOP_ffff000090816ca0;
    uVar1 = daif;
    PTR_LOOP_ffff000090816ca0 = (undefined *)puVar4;
    if (((uint)uVar1 >> 7 & 1) == 0) {
      uVar2 = daif;
      daif = uVar2 | 0x80;
      DAT_ffff000090816ca8 = 0;
      *puVar4 = &PTR_LOOP_ffff000090816c98;
      puVar4[1] = puVar3;
      uVar5 = 0;
      *(undefined8 **)puVar3 = puVar4;
      uVar2 = daif;
      daif = uVar2 & 0xffffffffffffff7f;
    }
    else {
      uVar5 = 0;
      DAT_ffff000090816ca8 = 0;
      *puVar4 = &PTR_LOOP_ffff000090816c98;
      puVar4[1] = puVar3;
      *(undefined8 **)puVar3 = puVar4;
    }
  }
  return uVar5;
}


