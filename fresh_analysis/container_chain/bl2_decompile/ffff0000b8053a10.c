
undefined8 FUN_ffff0000b8053a10(undefined8 param_1,byte param_2)

{
  undefined *puVar1;
  undefined *puVar2;
  long lVar3;
  undefined8 uVar4;
  
  lVar3 = FUN_ffff0000b8056e5c();
  if ((param_2 < 3) &&
     (*(byte *)(lVar3 + 0x179) = param_2, puVar1 = PTR_FUN_ffff0000b8108790, param_2 == 1)) {
    uVar4 = 0;
    *(undefined1 **)(lVar3 + 0x1f8) = &LAB_ffff0000b805372c;
    *(undefined1 **)(lVar3 + 0x200) = &LAB_ffff0000b8053830;
    *(undefined **)(lVar3 + 0x210) = puVar1;
    puVar2 = PTR_LAB_ffff0000b81087a8;
    puVar1 = PTR_FUN_ffff0000b81087a0;
    *(undefined **)(lVar3 + 0x218) = PTR_FUN_ffff0000b8108798;
    *(undefined **)(lVar3 + 0x228) = puVar1;
    *(undefined **)(lVar3 + 0x238) = puVar2;
    puVar2 = PTR_LAB_ffff0000b81087c0;
    puVar1 = PTR_LAB_ffff0000b81087b8;
    *(undefined **)(lVar3 + 0x240) = PTR_LAB_ffff0000b81087b0;
    *(undefined **)(lVar3 + 0x248) = puVar1;
    *(undefined **)(lVar3 + 0x250) = puVar2;
    *(undefined1 **)(lVar3 + 0x208) = &LAB_ffff0000b8053518;
    puVar1 = PTR_FUN_ffff0000b81087c8;
    *(undefined1 **)(lVar3 + 600) = &LAB_ffff0000b8053934;
    *(undefined **)(lVar3 + 0x220) = puVar1;
  }
  else {
    uVar4 = 0xffffffff;
  }
  return uVar4;
}


