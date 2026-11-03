// ffff00009078f9bc  size=220  FUN_ffff00009078f9bc

undefined8 FUN_ffff00009078f9bc(undefined8 param_1,byte param_2)

{
  undefined *puVar1;
  undefined *puVar2;
  long lVar3;
  undefined8 uVar4;
  
  lVar3 = FUN_ffff000090792e88();
  if ((param_2 < 3) &&
     (*(byte *)(lVar3 + 0x179) = param_2, puVar1 = PTR_FUN_ffff000090839a20, param_2 == 1)) {
    uVar4 = 0;
    *(code **)(lVar3 + 0x1f8) = FUN_ffff00009078f6d8;
    *(code **)(lVar3 + 0x200) = FUN_ffff00009078f7dc;
    *(undefined **)(lVar3 + 0x210) = puVar1;
    puVar2 = PTR_FUN_ffff000090839a38;
    puVar1 = PTR_FUN_ffff000090839a30;
    *(undefined **)(lVar3 + 0x218) = PTR_FUN_ffff000090839a28;
    *(undefined **)(lVar3 + 0x228) = puVar1;
    *(undefined **)(lVar3 + 0x238) = puVar2;
    puVar2 = PTR_FUN_ffff000090839a50;
    puVar1 = PTR_FUN_ffff000090839a48;
    *(undefined **)(lVar3 + 0x240) = PTR_FUN_ffff000090839a40;
    *(undefined **)(lVar3 + 0x248) = puVar1;
    *(undefined **)(lVar3 + 0x250) = puVar2;
    *(code **)(lVar3 + 0x208) = FUN_ffff00009078f4c4;
    puVar1 = PTR_FUN_ffff000090839a58;
    *(code **)(lVar3 + 600) = FUN_ffff00009078f8e0;
    *(undefined **)(lVar3 + 0x220) = puVar1;
  }
  else {
    uVar4 = 0xffffffff;
  }
  return uVar4;
}


