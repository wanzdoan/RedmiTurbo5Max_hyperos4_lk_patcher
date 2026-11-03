
/* responseRadioCapability(android::Parcel&, void*, unsigned long) */

void responseRadioCapability(Parcel *param_1,void *param_2,ulong param_3)

{
  long lVar1;
  int iVar2;
  void *__ptr;
  undefined8 uVar3;
  ulong uVar4;
  undefined8 local_40;
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  if (param_2 == (void *)0x0) {
    uVar4 = __rfx_is_gt_mode();
    if ((uVar4 & 1) == 0) {
      mtkLogE("ParcelUtil","invalid response: NULL");
    }
    else {
      local_40 = 0;
      android::String8::format("%s%s",&local_40,&DAT_001fbebf,"ParcelUtil");
      mtkLogE(local_40,"invalid response: NULL");
LAB_0039a44c:
      android::String8::~String8((String8 *)&local_40);
    }
  }
  else {
    if (param_3 == 0x54) {
      iVar2 = (int)param_1;
      android::Parcel::writeInt32(iVar2);
      android::Parcel::writeInt32(iVar2);
      android::Parcel::writeInt32(iVar2);
      android::Parcel::writeInt32(iVar2);
      local_40 = 0;
      __ptr = (void *)strdup8to16((long)param_2 + 0x10,&local_40);
      android::Parcel::writeString16((wchar16 *)param_1,(ulong)__ptr);
      free(__ptr);
      android::Parcel::writeInt32(iVar2);
      uVar3 = 0;
      goto LAB_0039a490;
    }
    uVar4 = __rfx_is_gt_mode();
    if ((uVar4 & 1) != 0) {
      local_40 = 0;
      android::String8::format("%s%s",&local_40,&DAT_001fbebf,"ParcelUtil");
      mtkLogE(local_40,"invalid response length was %d expected %d",param_3 & 0xffffffff,0x10);
      goto LAB_0039a44c;
    }
    mtkLogE("ParcelUtil","invalid response length was %d expected %d",param_3 & 0xffffffff,0x10);
  }
  uVar3 = 0xffffffff;
LAB_0039a490:
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar3);
}


