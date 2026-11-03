
/* dispatchRadioCapability(void (*)(int, void*, unsigned long, void*, unsigned int),
   android::Parcel&, RfxRequestInfo*) */

void dispatchRadioCapability
               (_func_void_int_void_ptr_ulong_void_ptr_uint *param_1,Parcel *param_2,
               RfxRequestInfo *param_3)

{
  long lVar1;
  int iVar2;
  undefined4 local_8c;
  ulong local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined8 local_48;
  ulong uStack_40;
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  uStack_80 = 0;
  local_88 = 0;
  uStack_70 = 0;
  local_78 = 0;
  uStack_60 = 0;
  local_68 = 0;
  uStack_50 = 0;
  local_58 = 0;
  uStack_40 = 0;
  local_48 = 0;
  iVar2 = android::Parcel::readInt32((int *)param_2);
  local_8c = 0;
  if (iVar2 == 0) {
    iVar2 = android::Parcel::readInt32((int *)param_2);
    local_88 = local_88 & 0xffffffff00000000;
    if (iVar2 == 0) {
      iVar2 = android::Parcel::readInt32((int *)param_2);
      local_88 = local_88 & 0xffffffff;
      if (iVar2 == 0) {
        iVar2 = android::Parcel::readInt32((int *)param_2);
        uStack_80 = uStack_80 & 0xffffffff00000000;
        if ((iVar2 == 0) &&
           (iVar2 = readStringFromParcelInplace(param_2,(char *)((long)&uStack_80 + 4),0x40),
           iVar2 == 0)) {
          iVar2 = android::Parcel::readInt32((int *)param_2);
          uStack_40 = uStack_40 & 0xffffffff;
          if (iVar2 == 0) {
            (*param_1)(**(int **)(param_3 + 8),&local_8c,0x54,param_3,*(uint *)(param_3 + 0x1c));
            goto LAB_00396280;
          }
        }
      }
    }
  }
  invalidCommandBlock(param_3);
LAB_00396280:
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


