
undefined8 FUN_ffff0000b8014d0c(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  ulong uVar1;
  undefined *puVar2;
  undefined8 *puVar3;
  undefined8 uVar4;
  
  puVar3 = (undefined8 *)FUN_ffff0000b804bccc(0x28);
  if (puVar3 == (undefined8 *)0x0) {
    uVar4 = 0xfffffffb;
  }
  else {
    FUN_ffff0000b804d070(puVar3,0,0x28);
    *puVar3 = puVar3;
    puVar3[1] = puVar3;
    puVar3[2] = param_1;
    puVar3[3] = param_3;
    puVar3[4] = param_2;
    puVar2 = PTR_LOOP_ffff0000b80ae048;
    uVar4 = daif;
    PTR_LOOP_ffff0000b80ae048 = (undefined *)puVar3;
    if (((uint)uVar4 >> 7 & 1) == 0) {
      uVar1 = daif;
      daif = uVar1 | 0x80;
      uVar4 = 0;
      DAT_ffff0000b80ae050 = 0;
      *puVar3 = &PTR_LOOP_ffff0000b80ae040;
      puVar3[1] = puVar2;
      *(undefined8 **)puVar2 = puVar3;
      uVar1 = daif;
      daif = uVar1 & 0xffffffffffffff7f;
    }
    else {
      uVar4 = 0;
      DAT_ffff0000b80ae050 = 0;
      *puVar3 = &PTR_LOOP_ffff0000b80ae040;
      puVar3[1] = puVar2;
      *(undefined8 **)puVar2 = puVar3;
    }
  }
  return uVar4;
}


