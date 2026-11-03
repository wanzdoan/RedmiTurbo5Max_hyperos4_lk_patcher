
int FUN_ffff0000be09b954
              (long param_1,undefined8 param_2,ulong param_3,undefined8 param_4,undefined8 param_5,
              undefined8 param_6)

{
  long lVar1;
  long lVar2;
  int iVar3;
  int iVar4;
  ulong uVar5;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_98 = 0;
  local_a8 = 0;
  uStack_a0 = 0;
  local_b8 = 0;
  uStack_b0 = 0;
  local_c8 = 0;
  uStack_c0 = 0;
  local_d8 = 0;
  uStack_d0 = 0;
  local_e8 = 0;
  uStack_e0 = 0;
  local_f8 = 0;
  uStack_f0 = 0;
  FUN_ffff0000be09f220(&local_f8);
  FUN_ffff0000be09bcf0(&uStack_68);
  FUN_ffff0000be09bcf0(&local_80);
  FUN_ffff0000be09bcf0(&local_98);
  FUN_ffff0000be09bcf0(&uStack_b0);
  if (*(long *)(param_1 + 0xa8) == 0) {
    return -0x4f80;
  }
  iVar3 = FUN_ffff0000be09c9e4(param_5,1);
  if (-1 < iVar3) {
    lVar1 = param_1 + 0x98;
    iVar3 = FUN_ffff0000be09c8f4(param_5,lVar1);
    if (((iVar3 < 0) && (iVar3 = FUN_ffff0000be09c9e4(param_6,1), -1 < iVar3)) &&
       (iVar3 = FUN_ffff0000be09c8f4(param_6,lVar1), iVar3 < 0)) {
      uVar5 = *(long *)(param_1 + 0xb8) + 7U >> 3;
      if (param_3 <= uVar5) {
        uVar5 = param_3;
      }
      iVar3 = FUN_ffff0000be09c3f4(&uStack_68,param_2,uVar5);
      if ((((iVar3 != 0) ||
           ((lVar2 = uVar5 * 8 - *(ulong *)(param_1 + 0xb8),
            *(ulong *)(param_1 + 0xb8) <= uVar5 * 8 && lVar2 != 0 &&
            (iVar3 = FUN_ffff0000be09c798(&uStack_68,lVar2), iVar3 != 0)))) ||
          ((iVar3 = FUN_ffff0000be09c8f4(&uStack_68,lVar1), -1 < iVar3 &&
           (iVar3 = FUN_ffff0000be09d008(&uStack_68,&uStack_68,lVar1), iVar3 != 0)))) ||
         (((iVar3 = FUN_ffff0000be09ec54(&local_80,param_6,lVar1), iVar3 != 0 ||
           (iVar3 = FUN_ffff0000be09d11c(&local_98,&uStack_68,&local_80), iVar3 != 0)) ||
          ((iVar3 = FUN_ffff0000be09de20(&local_98,&local_98,lVar1), iVar3 != 0 ||
           (((iVar3 = FUN_ffff0000be09d11c(&uStack_b0,param_5,&local_80), iVar3 != 0 ||
             (iVar3 = FUN_ffff0000be09de20(&uStack_b0,&uStack_b0,lVar1), iVar3 != 0)) ||
            (iVar3 = FUN_ffff0000be09ffb8
                               (param_1,&local_f8,&local_98,param_1 + 0x50,&uStack_b0,param_4,0),
            iVar3 != 0)))))))) goto LAB_ffff0000be09ba10;
      iVar3 = FUN_ffff0000be09f460(&local_f8);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000be09de20(&local_f8,&local_f8,lVar1);
        if (iVar3 == 0) {
          iVar4 = FUN_ffff0000be09c8f4(&local_f8,param_5);
          iVar3 = 0;
          if (iVar4 != 0) {
            iVar3 = -0x4e00;
          }
        }
        goto LAB_ffff0000be09ba10;
      }
    }
  }
  iVar3 = -0x4e00;
LAB_ffff0000be09ba10:
  FUN_ffff0000be09f2e4(&local_f8);
  FUN_ffff0000be09bd08(&uStack_68);
  FUN_ffff0000be09bd08(&local_80);
  FUN_ffff0000be09bd08(&local_98);
  FUN_ffff0000be09bd08(&uStack_b0);
  return iVar3;
}


