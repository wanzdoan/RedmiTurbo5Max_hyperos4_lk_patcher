
void FUN_ffff0000907098f0
               (undefined8 param_1,undefined8 param_2,undefined4 param_3,undefined4 param_4)

{
  undefined4 uVar1;
  undefined8 *puVar2;
  
  puVar2 = (undefined8 *)FUN_ffff00009077d514(0x28);
  if (puVar2 != (undefined8 *)0x0) {
    puVar2[1] = param_1;
    uVar1 = FUN_ffff00009077f250(param_1);
    *(undefined4 *)((long)puVar2 + 0x14) = param_3;
    *(undefined4 *)(puVar2 + 3) = param_4;
    puVar2[4] = param_2;
    *(undefined4 *)(puVar2 + 2) = uVar1;
    *puVar2 = DAT_ffff00009083d100;
    DAT_ffff00009083d100 = puVar2;
  }
  return;
}


