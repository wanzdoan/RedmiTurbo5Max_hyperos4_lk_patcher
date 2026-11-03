
undefined8 FUN_ffff0000be06d79c(undefined8 param_1,byte param_2)

{
  undefined *puVar1;
  undefined *puVar2;
  long lVar3;
  undefined8 uVar4;
  
  lVar3 = FUN_ffff0000be070acc();
  if ((param_2 < 3) &&
     (*(byte *)(lVar3 + 0x179) = param_2, puVar1 = PTR_FUN_ffff0000be0f56c0, param_2 == 1)) {
    uVar4 = 0;
    *(undefined1 **)(lVar3 + 0x1f8) = &LAB_ffff0000be06d4b8;
    *(undefined1 **)(lVar3 + 0x200) = &LAB_ffff0000be06d5bc;
    *(undefined **)(lVar3 + 0x210) = puVar1;
    puVar2 = PTR_LAB_ffff0000be0f56d8;
    puVar1 = PTR_FUN_ffff0000be0f56d0;
    *(undefined **)(lVar3 + 0x218) = PTR_FUN_ffff0000be0f56c8;
    *(undefined **)(lVar3 + 0x228) = puVar1;
    *(undefined **)(lVar3 + 0x238) = puVar2;
    puVar2 = PTR_LAB_ffff0000be0f56f0;
    puVar1 = PTR_LAB_ffff0000be0f56e8;
    *(undefined **)(lVar3 + 0x240) = PTR_LAB_ffff0000be0f56e0;
    *(undefined **)(lVar3 + 0x248) = puVar1;
    *(undefined **)(lVar3 + 0x250) = puVar2;
    *(undefined1 **)(lVar3 + 0x208) = &LAB_ffff0000be06d2a4;
    puVar1 = PTR_FUN_ffff0000be0f56f8;
    *(undefined1 **)(lVar3 + 600) = &LAB_ffff0000be06d6c0;
    *(undefined **)(lVar3 + 0x220) = puVar1;
  }
  else {
    uVar4 = 0xffffffff;
  }
  return uVar4;
}


