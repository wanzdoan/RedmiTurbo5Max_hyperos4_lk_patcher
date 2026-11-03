
undefined8 FUN_ffff00009078fc7c(undefined8 param_1,byte param_2)

{
  undefined *puVar1;
  undefined *puVar2;
  long lVar3;
  undefined8 uVar4;
  
  lVar3 = FUN_ffff000090793148();
  if ((param_2 < 3) &&
     (*(byte *)(lVar3 + 0x179) = param_2, puVar1 = PTR_FUN_ffff000090839a40, param_2 == 1)) {
    uVar4 = 0;
    *(undefined1 **)(lVar3 + 0x1f8) = &LAB_ffff00009078f998;
    *(undefined1 **)(lVar3 + 0x200) = &LAB_ffff00009078fa9c;
    *(undefined **)(lVar3 + 0x210) = puVar1;
    puVar2 = PTR_LAB_ffff000090839a58;
    puVar1 = PTR_FUN_ffff000090839a50;
    *(undefined **)(lVar3 + 0x218) = PTR_FUN_ffff000090839a48;
    *(undefined **)(lVar3 + 0x228) = puVar1;
    *(undefined **)(lVar3 + 0x238) = puVar2;
    puVar2 = PTR_LAB_ffff000090839a70;
    puVar1 = PTR_LAB_ffff000090839a68;
    *(undefined **)(lVar3 + 0x240) = PTR_LAB_ffff000090839a60;
    *(undefined **)(lVar3 + 0x248) = puVar1;
    *(undefined **)(lVar3 + 0x250) = puVar2;
    *(undefined1 **)(lVar3 + 0x208) = &LAB_ffff00009078f784;
    puVar1 = PTR_FUN_ffff000090839a78;
    *(undefined1 **)(lVar3 + 600) = &LAB_ffff00009078fba0;
    *(undefined **)(lVar3 + 0x220) = puVar1;
  }
  else {
    uVar4 = 0xffffffff;
  }
  return uVar4;
}


